package com.rjkj.cf.bbibm.kjds.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.rjkj.cf.admin.api.entity.UserJoinVo;
import com.rjkj.cf.admin.api.vo.UserVO;
import com.rjkj.cf.bbibm.kjds.api.entity.*;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteGoodsFeignService;
import com.rjkj.cf.bbibm.kjds.api.feign.RemotePayFeignService;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteProductFeignService;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteUserFeignService;
import com.rjkj.cf.bbibm.kjds.api.utils.Const;
import com.rjkj.cf.bbibm.kjds.api.utils.IDUtils;
import com.rjkj.cf.bbibm.kjds.api.utils.JpushClientUtil;
import com.rjkj.cf.bbibm.kjds.api.utils.MsgConstant;
import com.rjkj.cf.bbibm.kjds.goods.entity.Goods;
import com.rjkj.cf.bbibm.kjds.goods.entity.GoodsOrder;
import com.rjkj.cf.bbibm.kjds.goods.entity.*;
import com.rjkj.cf.bbibm.kjds.goods.mapper.GoodsMapper;
import com.rjkj.cf.bbibm.kjds.goods.reqvo.ShopApprovalVo;
import com.rjkj.cf.bbibm.kjds.goods.service.*;
import com.rjkj.cf.common.core.constant.SecurityConstants;
import com.rjkj.cf.common.security.service.RjkjUser;
import com.rjkj.cf.common.sequence.sequence.Sequence;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @描述：商户模块
 * @项目：
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019-10-08 12:00:03
 **/
@Service
@AllArgsConstructor
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    private final GoodsMapper goodsMapper;
    private final GoodsProductService goodsProductService;
    private final RemotePayFeignService remotePayFeignService;
    private final Sequence paySequence;
    private final GoodsOrderService goodsOrderService;
    private final GoodsRecommendService goodsRecommendService;
    private final RedisTemplate redisTemplate;
    private final PartnerService partnerService;
    private final PartnerJoinService partnerJoinService;
    private final RemoteUserFeignService remoteUserService;
    private final PartnerJoinService getPartnerJoinService;
    private final RemoteGoodsFeignService remoteGoodsFeignService;
    private final RemoteProductFeignService remoteProductFeignService;

    @Override
    public int addGoods(RjkjUser user, Goods goods) {
        if (StringUtils.isEmpty(goods.getSname())) {
            throw new RuntimeException("店铺名称不能为空");
        }
        goods.setSid(IDUtils.getGUUID(String.valueOf(System.currentTimeMillis())));
        goods.setUuid(user.getId());
        goods.setCid(user.getId());
        goods.setUid(user.getId());
        goods.setCtime(LocalDateTime.now());
        goods.setUtime(LocalDateTime.now());
        return this.baseMapper.insert(goods);
    }

    @Override
    public int updateGoods(RjkjUser user, Goods goods) {
        Goods goods1 = this.baseMapper.selectById(goods.getSid());
        Optional.ofNullable(goods1).orElseThrow(() -> new RuntimeException("店铺信息不能为空"));
        if (StringUtils.isEmpty(goods.getSname())) {
            throw new RuntimeException("店铺名称不能为空");
        }
        goods.setUuid(user.getId());
        goods.setUtime(LocalDateTime.now());
        return this.baseMapper.updateById(goods);
    }

    @Override
    public IPage<Goods> listGoods(Page page, Goods goods, RjkjUser user) {
        IPage<Goods> goodsIPage;
        if (1 == 1) {
            goodsIPage = this.page(page, Wrappers.query(goods));
        } else {
            goodsIPage = this.page(page, Wrappers.query(goods).lambda()
                    .eq(Goods::getUid, user.getId()));
        }
        return goodsIPage;
    }

    @Override
    public IPage<Goods> getApprovalGoodsPage(Page page, Goods goods) {
        return goodsMapper.getApprovalGoodsPage(page, goods.getPid(), goods.getStatus(), goods.getPhone());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void shopApproval(ShopApprovalVo shopApprovalVo, RjkjUser user) {
        try {
            Goods goods = goodsMapper.selectById(shopApprovalVo.getSid());
            JpushClientUtil jpushClientUtil = new JpushClientUtil(remoteGoodsFeignService, remoteProductFeignService);
            if ("0".equals(shopApprovalVo.getType())) {

                String userId = shopApprovalVo.getUid();
                //城市合伙人提成订单
                List<Partner> partnerList1 = partnerService.list(Wrappers.<Partner>query().lambda()
                        .eq(Partner::getCity, shopApprovalVo.getApplicantArea())
                        .eq(Partner::getOptionStatus, "1"));
                if (partnerList1 != null && partnerList1.size()>0) {
                    Partner partner = partnerList1.get(0);
                    //判断当前时间是否超过该合伙人的有效时间
                    LocalDateTime endTime = partner.getEndTime();
                    if (endTime.isAfter(LocalDateTime.now())) {
                        String cuid = partner.getCuid();
                        List<PartnerJoin> list = getPartnerJoinService.list(Wrappers.<PartnerJoin>query().lambda()
                                .eq(PartnerJoin::getShareId, cuid)
                                .eq(PartnerJoin::getRecvId, goods.getUid()));
                        if (list.size() <= 0) {

                            //创建城市合伙人分享表信息
                            PartnerJoin partnerJoin = new PartnerJoin();
                            partnerJoin.setId(IDUtils.getGUUID(""));
                            partnerJoin.setShareId(cuid);
                            partnerJoin.setRecvId(userId);
                            partnerJoin.setCtime(LocalDateTime.now());
                            partnerJoinService.save(partnerJoin);
                        }
                        //保存提成信息
                        GoodsOrder goodsOrder = new GoodsOrder();
                        goodsOrder.setOid(IDUtils.getGUUID(""));
                        goodsOrder.setPayUid(shopApprovalVo.getUid());
                        goodsOrder.setRecvUid(cuid);
                        goodsOrder.setOrderType("5");
                        goodsOrder.setAmount(new BigDecimal(1)); //TODO 城市合伙人提成价格
                        goodsOrder.setAmountType("2");
                        goodsOrder.setStatus("2");
                        goodsOrder.setCloseTime(LocalDateTime.now());
                        goodsOrder.setEndTime(LocalDateTime.now());
                        goodsOrder.setPayTime(LocalDateTime.now());
                        goodsOrder.setCtime(LocalDateTime.now());
                        goodsOrder.setUtime(LocalDateTime.now());
                        this.goodsOrderService.save(goodsOrder);
                    }
                }
                UserVO userVO = remoteUserService.getUserById(userId, SecurityConstants.FROM).getData();
                UserJoinVo userJoinVo = remoteUserService.queryByShareInfo(userVO.getPhone()).getData();
                if (userJoinVo != null) {
                    //保存提成信息
                    GoodsOrder goodsOrder = new GoodsOrder();
                    goodsOrder.setOid(IDUtils.getGUUID(""));
                    goodsOrder.setPayUid(shopApprovalVo.getUid());
                    goodsOrder.setRecvUid(userJoinVo.getShareId());
                    goodsOrder.setOrderType("2");
                    goodsOrder.setAmount(new BigDecimal(1)); //TODO 自推荐提成价格
                    goodsOrder.setAmountType("2");
                    goodsOrder.setStatus("2");
                    goodsOrder.setCloseTime(LocalDateTime.now());
                    goodsOrder.setEndTime(LocalDateTime.now());
                    goodsOrder.setPayTime(LocalDateTime.now());
                    goodsOrder.setCtime(LocalDateTime.now());
                    goodsOrder.setUtime(LocalDateTime.now());
                    this.goodsOrderService.save(goodsOrder);
                }
                goods.setStatus("0");
                //更改User角色为商户
                remoteUserService.updateUserRole(goods.getUid(), Const.MERCHANT, "1");
                switch (goods.getPid()) {
                    case "18":
                        //Amazon LOGO
                        goods.setGoodsPhoto(Const.AMAZON_LOGO);
                        break;
                    case "19":
                        //Ebay LOGO
                        goods.setGoodsPhoto(Const.EBAY_LOGO);
                        break;
                    case "20":
                        //Shopee LOGO
                        goods.setGoodsPhoto(Const.SHOPEE_LOGO);
                        break;
                    default:
                }
                goods.setSecretKey(shopApprovalVo.getSecretKey());
                goods.setAwsAccessKeyId(shopApprovalVo.getAwsAccessKeyId());
                goods.setAccountSiteId(shopApprovalVo.getShopId());
                goods.setMwsToken(shopApprovalVo.getMwsToken());
                goods.setArea(shopApprovalVo.getShopArea());
                String sname = goods.getSname();
                if ("US".equalsIgnoreCase(shopApprovalVo.getShopArea())) {
                    goods.setSname(sname + "(美国)");
                } else if ("TW".equalsIgnoreCase(shopApprovalVo.getShopArea())) {
                    goods.setSname(sname + "(台湾)");
                } else if ("FR".equalsIgnoreCase(shopApprovalVo.getShopArea())) {
                    goods.setSname(sname + "(法国)");
                } else if ("DE".equalsIgnoreCase(shopApprovalVo.getShopArea())) {
                    goods.setSname(sname + "(德国)");
                } else if ("GB".equalsIgnoreCase(shopApprovalVo.getShopArea())) {
                    goods.setSname(sname + "(英国)");
                } else if ("CA".equalsIgnoreCase(shopApprovalVo.getShopArea())) {
                    goods.setSname(sname + "(加拿大)");
                } else if ("JP".equalsIgnoreCase(shopApprovalVo.getShopArea())) {
                    goods.setSname(sname + "(日本)");
                } else if ("MX".equalsIgnoreCase(shopApprovalVo.getShopArea())) {
                    goods.setSname(sname + "(墨西哥)");
                } else if ("SG".equalsIgnoreCase(shopApprovalVo.getShopArea())) {
                    goods.setSname(sname + "(新加坡)");
                } else if ("MY".equalsIgnoreCase(shopApprovalVo.getShopArea())) {
                    goods.setSname(sname + "(马来西亚)");
                }else if ("ES".equalsIgnoreCase(shopApprovalVo.getShopArea())) {
                    goods.setSname(sname + "(西班牙)");
                }else if ("AU".equalsIgnoreCase(shopApprovalVo.getShopArea())) {
                    goods.setSname(sname + "(澳大利亚)");
                }
                goodsMapper.updateById(goods);
                //消息通知
                jpushClientUtil.sendOrdinaryMsg(MsgConstant.SHOPAPPROVAL_TITLE, MsgConstant.SHOPAPPROVAL_SUCCESS_CONTENS, goods.getUid());
                jpushClientUtil.saveMsg(goods.getUid(), MsgConstant.SHOPAPPROVAL_SUCCESS_CONTENS);
            } else if ("1".equals(shopApprovalVo.getType())) {
                goods.setStatus("3");
                goodsMapper.updateById(goods);
                //拒绝消息通知
                jpushClientUtil.sendOrdinaryMsg(MsgConstant.SHOPAPPROVAL_TITLE, MsgConstant.SHOPAPPROVAL_FAIL_CONTENS, goods.getUid());
                jpushClientUtil.saveMsg(goods.getUid(), "抱歉，您未通过审核。");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<PlatformInfoVo> selectUserPlatformInfo(RjkjUser user) {
        return goodsMapper.selectUserPlatformInfo(user.getId());
    }

    @Override
    public List<ShoppeId> getShoppids(RjkjUser user, String pid) {
        return goodsMapper.selectShoppidList(user.getId(), pid);
    }

    @Override
    public List<PlatformInfoVo> priceList() {
        return goodsMapper.priceList();
    }

    @Override
    public String applyGoods(RjkjUser user, ApplyGoodsReq req) {
        if (StringUtils.isEmpty(req.getGname())) {
            throw new RuntimeException("店铺名不能为空");
        }
        if (StringUtils.isEmpty(req.getPid())) {
            throw new RuntimeException("请选择平台");
        }
        if (req.getIdcardType() == 0) {
            throw new RuntimeException("请选择证件类型");
        }
        if (req.getPayType() == 0) {
            throw new RuntimeException("请选择支付类型");
        }
        if (StringUtils.isEmpty(req.getPositiveIdCardUrl())) {
            throw new RuntimeException("请上传正面手持身份证");
        }
        if (StringUtils.isEmpty(req.getNegativeIdCardUrl())) {
            throw new RuntimeException("请上传反面手持身份证");
        }
        if (StringUtils.isEmpty(req.getApplyType())) {
            throw new RuntimeException("请选择国籍");
        }
        if (req.getType() == 0) {
            throw new RuntimeException("请选择申请类型");
        }

        PlatformInfoVo platformInfoVo = this.goodsMapper.selectPidInfo(req.getPid());
        Optional.ofNullable(platformInfoVo).orElseThrow(() -> new RuntimeException("请选择平台"));
        Goods goods = new Goods();
        goods.setSid(IDUtils.getGUUID(""));
        goods.setUtime(LocalDateTime.now());
        goods.setPhone(user.getUsername());
        goods.setUuid(user.getId());
        goods.setUid(user.getId());
        goods.setCtime(LocalDateTime.now());
        goods.setCid("");
        goods.setCardPositiveUrl(req.getPositiveIdCardUrl());
        goods.setCardSideUrl(req.getNegativeIdCardUrl());
//        if (req.getType() == 2) {
        goods.setBlicenseUrl(req.getBusinessLicense());
//        }
        goods.setSname(req.getGname());
        goods.setType(req.getType());
        goods.setIdcardType(req.getIdcardType());
        goods.setApplyType(req.getApplyType());
        goods.setPid(req.getPid());
        goods.setStatus("4");
        this.baseMapper.insert(goods);
        GoodsOrder goodsOrder = new GoodsOrder();
        goodsOrder.setOid(paySequence.nextNo());
        goodsOrder.setPayUid(user.getId());
        goodsOrder.setUtime(LocalDateTime.now());
        goodsOrder.setGid(goods.getSid());
        goodsOrder.setAmount(BigDecimal.valueOf(Double.parseDouble(platformInfoVo.getPrice())).abs());
        goodsOrder.setAmountType(String.valueOf(req.getPayType()));
        goodsOrder.setBuyerRate(0);
        goodsOrder.setCtime(LocalDateTime.now());
        goodsOrder.setOrderType("4");
        goodsOrder.setStatus("1");
        this.goodsOrderService.save(goodsOrder);
        PayGoodsOrder payGoodsOrder = new PayGoodsOrder();
        payGoodsOrder.setPayOrderId(goodsOrder.getOid());
        payGoodsOrder.setGoodsName(req.getGname() + "-店铺申请");
        payGoodsOrder.setAmount(goodsOrder.getAmount());
//        payGoodsOrder.setCallUrl("http://2784yd2596.qicp.vip:53372/goods/goods/apply/pay/callback");
        payGoodsOrder.setCallUrl("http://47.97.167.214:8666/goods/goods/apply/pay/callback");
        remotePayFeignService.goodsAddOrder(payGoodsOrder);
        return goodsOrder.getOid();
    }

    @Override
    public String addrechargeBalanceInfo(RjkjUser user, String payType, BigDecimal amount) {
        if (StringUtils.isEmpty(payType)) {
            throw new RuntimeException("请选择支付方式");
        }
        if (amount == null) {
            throw new RuntimeException("金额不能为空");
        }
        GoodsOrder goodsOrder = new GoodsOrder();
        goodsOrder.setOid(paySequence.nextNo());
        goodsOrder.setPayUid(user.getId());
        goodsOrder.setUtime(LocalDateTime.now());
        goodsOrder.setAmount(amount);
        goodsOrder.setAmountType(payType);
        goodsOrder.setCtime(LocalDateTime.now());
        goodsOrder.setOrderType("6");
        goodsOrder.setStatus("1");
        this.goodsOrderService.save(goodsOrder);
        PayGoodsOrder payGoodsOrder = new PayGoodsOrder();
        payGoodsOrder.setPayOrderId(goodsOrder.getOid());
        payGoodsOrder.setGoodsName(user.getUsername() + "余额充值");
        payGoodsOrder.setAmount(amount);
        payGoodsOrder.setCallUrl("testCallBack");
        remotePayFeignService.goodsAddOrder(payGoodsOrder);
        String oid = goodsOrder.getOid();
        UserOrder userOrder = new UserOrder();
        userOrder.setOid(oid);
        userOrder.setAmount(amount);
        userOrder.setCtime(LocalDateTime.now());
        userOrder.setPayUid(user.getId());
        userOrder.setAmountType(payType);
        userOrder.setStatus("1");
        userOrder.setOrderType("1");
        userOrder.setDescription(user.getUsername() + "余额充值");
        remoteUserService.saveUserOrder(userOrder);
        return oid;
    }

    @Override
    public List<PlatformGoodsRsp> getGoodsListByUserId(String userId) {
        List<PlatformGoodsRsp> platformList = this.goodsMapper.platformList(userId);
//        platformList.stream().forEach(item -> {
//            AtomicInteger goodsShlefNum = new AtomicInteger(0);
//            item.getGoodsRspList().stream()
//                    .forEach(goodsItem -> {
//                        int count = goodsProductService.count(Wrappers.<GoodsProduct>query()
//                                .lambda().eq(GoodsProduct::getUid, uid)
//                                .eq(GoodsProduct::getGid, goodsItem.getGid())
//                                .eq(GoodsProduct::getRackStatus, 0));
//                        goodsItem.setGSelfNum(count);
//                        goodsShlefNum.addAndGet(count);
//                    });
//            item.setPShelfNum(goodsShlefNum.get());
//        });
        return addUrl(platformList);
    }

    @Override
    public List<String> getGoodsUserIdListByGoodsId(String shopId, String area) {
        List<Goods> goods = this.goodsMapper.selectList(Wrappers.<Goods>query().lambda()
                .eq(Goods::getAccountSiteId, shopId)
                .eq(Goods::getArea, area));
        if (goods == null) {
            return null;
        }
        ArrayList<String> userIds = new ArrayList<>();
        for (Goods good : goods) {
            userIds.add(good.getUid());
        }
        return userIds;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public void test() {
        UserOrder userOrder = new UserOrder();
        userOrder.setOid(IDUtils.getGUUID(""));
        userOrder.setPayUid("111111");
        userOrder.setRecvUid("11111");
        userOrder.setAmount(new BigDecimal(111111));
        userOrder.setPayTime(LocalDateTime.now());
        userOrder.setCtime(LocalDateTime.now());
        userOrder.setUtime(LocalDateTime.now());
        remoteUserService.saveUserOrder(userOrder);
        remoteGoodsFeignService.updateByOrderId("2019122000005", SecurityConstants.FROM);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int updateStatusToReview(String gid) {
        Goods goods = goodsMapper.selectById(gid);
        goods.setStatus("2");
        return goodsMapper.updateById(goods);
    }

    @Override
    public List<PlatformGoodsRsp> goodsList(RjkjUser user) {

        List<PlatformGoodsRsp> platformList = this.goodsMapper.platformList(user.getId());
        platformList.stream().forEach(item -> {
            AtomicInteger goodsShlefNum = new AtomicInteger(0);
            item.getGoodsRspList().stream()
                    .forEach(goodsItem -> {
                        int count = goodsProductService.count(Wrappers.<GoodsProduct>query()
                                .lambda().eq(GoodsProduct::getUid, user.getId())
                                .eq(GoodsProduct::getGid, goodsItem.getGid())
                                .eq(GoodsProduct::getRackStatus, 0));
                        goodsItem.setGSelfNum(count);
                        goodsShlefNum.addAndGet(count);
                    });
            item.setPShelfNum(goodsShlefNum.get());
        });
        return platformList;
    }


    @Override
    public void payBussCallback(PaySuccesCallBussEntity paySuccesCallBussEntity) {
        String payStatus = paySuccesCallBussEntity.getPayStatus();
        if (!"1".equals(payStatus)) {
            return;
        }
        String oId = paySuccesCallBussEntity.getOId();
        GoodsOrder byId = this.goodsOrderService.getById(oId);
        if (byId == null) {
            return;
        }
        byId.setStatus("2");
        byId.setUtime(LocalDateTime.now());
        byId.setPayTime(LocalDateTime.now());
        byId.setCloseTime(LocalDateTime.now());
        byId.setEndTime(LocalDateTime.now());
        this.goodsOrderService.updateById(byId);
        String payUid = byId.getPayUid();
        String shareUid = this.goodsMapper.getShareUid(payUid);
        if (StringUtils.isEmpty(shareUid)) {
            return;
        }
        GoodsRecommend one = goodsRecommendService.getOne(Wrappers.<GoodsRecommend>query().lambda()
                .eq(GoodsRecommend::getFeeType, 2));
        if (one == null) {
            return;
        }
        /**
         * 自推荐提成订单
         */
        GoodsOrder goodsOrder = new GoodsOrder();
        goodsOrder.setOid(paySequence.nextNo());
        goodsOrder.setEndTime(LocalDateTime.now());
        goodsOrder.setCloseTime(LocalDateTime.now());
        goodsOrder.setPayTime(LocalDateTime.now());
        goodsOrder.setUtime(LocalDateTime.now());
        goodsOrder.setPayUid(payUid);
        goodsOrder.setRecvUid(shareUid);
        goodsOrder.setStatus("2");
        goodsOrder.setCtime(LocalDateTime.now());
        BigDecimal fee = one.getFee();
        goodsOrder.setAmount(fee.multiply(byId.getAmount()));
        goodsOrder.setOrderType("2");
        this.goodsOrderService.save(goodsOrder);


        GoodsRecommend twoFfee = goodsRecommendService.getOne(Wrappers.<GoodsRecommend>query().lambda()
                .eq(GoodsRecommend::getFeeType, 1));
        /**
         * 城市合伙人提成订单 //TODO
         */
        BigDecimal fee1 = twoFfee.getFee();

        return;
    }

    @Override
    public void updateGoodsToRedis() {
        try {
            //初始化redis
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            HashOperations hashOperations = redisTemplate.opsForHash();
            //清空Redis的店铺信息
            String[] goodsType = Const.GOODS_TYPE.split("-");
            for (String type : goodsType) {
                Iterator iterator = hashOperations.keys(type).iterator();
                while (iterator.hasNext()) {
                    String goodsInfo = iterator.next().toString();
                    hashOperations.delete(type, goodsInfo);
                }
            }
            List<Goods> amazonGoods = goodsMapper.selectList(new QueryWrapper<Goods>().lambda()
                    .eq(Goods::getStatus, "0").eq(Goods::getPid, "18"));
            if (amazonGoods.size() > 0) {
                putAmazonGoodsToRedis(amazonGoods, "amazongoodsinfo");
            }
            List<Goods> ebayGoods = goodsMapper.selectList(new QueryWrapper<Goods>().lambda()
                    .eq(Goods::getStatus, "0").eq(Goods::getPid, "19"));
            if (ebayGoods.size() > 0) {
                putAmazonGoodsToRedis(ebayGoods, "ebaygoodsinfo");
            }
            List<Goods> shopeeGoods = goodsMapper.selectList(new QueryWrapper<Goods>().lambda()
                    .eq(Goods::getStatus, "0").eq(Goods::getPid, "20"));
            if (shopeeGoods.size() > 0) {
                putAmazonGoodsToRedis(shopeeGoods, "shopeegoodsinfo");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private void putAmazonGoodsToRedis(List<Goods> goods, String key) {
        try {
            HashOperations hashOperations = redisTemplate.opsForHash();
            for (Goods goodsInfo : goods) {
                //更新Redis
                StringBuilder info = new StringBuilder().append(goodsInfo.getAccountSiteId())
                        .append("_msg_")
                        .append(goodsInfo.getMwsToken())
                        .append("_msg_")
                        .append(goodsInfo.getArea());
//                        .append("_msg_")
//                        .append(goodsInfo.getSecretKey())
//                        .append("_msg_")
//                        .append(goodsInfo.getAwsAccessKeyId());
                hashOperations.put(key, info.toString(), "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private List<PlatformGoodsRsp> addUrl(List<PlatformGoodsRsp> platformInfo) {
        if (platformInfo != null) {
            for (PlatformGoodsRsp platformGoodsRsp : platformInfo) {
                String pid = platformGoodsRsp.getPid();
                if ("18".equals(pid)) {
                    platformGoodsRsp.setPlatformPhoto("http://ymx.bbibm.com/kjds/07584ec9062a4a58bb0b43d92b530a5e.jpg");
                    platformGoodsRsp.setOrderStatusCount("/order/amazonorder/getAmazonOrderStatusCount");
                    platformGoodsRsp.setOrderListByStatus("/order/amazonorder/getAmazonOrderListByStatus");
                    platformGoodsRsp.setOrderById("/order/amazonorder/getAmazonOrderById");
                    platformGoodsRsp.setEndItem("/product/amaoznFeedUpload/lowerProductInfo");
                    platformGoodsRsp.setOrderToShip("/order/amazonorder/orderToShip");
                    platformGoodsRsp.setProxyToShip("/order/amazonorder/proxyToShip");
                    platformGoodsRsp.setUpdatePrice("/product/amaoznFeedUpload/updateProductPriceInfo");
                } else if ("19".equals(pid)) {
                    platformGoodsRsp.setPlatformPhoto("http://ymx.bbibm.com/kjds/4acd4f54f9264e5abbd79fb60160c3c0.jpg");
                    platformGoodsRsp.setOrderStatusCount("/order/ebayorder/getEbayOrderStatusCount");
                    platformGoodsRsp.setOrderListByStatus("/order/ebayorder/getEbayOrderListByStatus");
                    platformGoodsRsp.setOrderById("/order/ebayorder/getEbayOrderById");
                    platformGoodsRsp.setEndItem("/product/ebayProduct/endItem");
                    platformGoodsRsp.setOrderToShip("/order/ebayorder/orderToShip");
                    platformGoodsRsp.setProxyToShip("/order/ebayorder/proxyToShip");
                    platformGoodsRsp.setUpdatePrice("/product/ebayProduct/updatePrice");
                } else if ("20".equals(pid)) {
                    platformGoodsRsp.setPlatformPhoto("http://ymx.bbibm.com/kjds/6432830b06174b679021b1f4db39a4f8.jpg");
                    platformGoodsRsp.setOrderStatusCount("/order/shopeeorder/getShopeeOrderStatusCount");
                    platformGoodsRsp.setOrderListByStatus("/order/shopeeorder/getShopeeOrderListByStatus");
                    platformGoodsRsp.setOrderById("/order/shopeeorder/getShopeeOrderById");
                    platformGoodsRsp.setEndItem("/product/shopeeProduct/endItem");
                    platformGoodsRsp.setOrderToShip("/order/shopeeorder/orderToShip");
                    platformGoodsRsp.setProxyToShip("/order/shopeeorder/proxyToShip");
                    platformGoodsRsp.setUpdatePrice("/product/shopeeProduct/updatePrice");
                }
            }
            return platformInfo;
        }
        return null;
    }
}
