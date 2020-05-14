package com.rjkj.cf.bbibm.kjds.product.supplier.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjkj.cf.bbibm.kjds.api.entity.OrderItemDetail;
import com.rjkj.cf.bbibm.kjds.api.entity.Product;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteProductFeignService;
import com.rjkj.cf.bbibm.kjds.api.utils.IDUtils;
import com.rjkj.cf.bbibm.kjds.product.supplier.entity.ProductVariant;
import com.rjkj.cf.bbibm.kjds.product.supplier.entity.SupplierProduct;
import com.rjkj.cf.bbibm.kjds.product.supplier.service.ProductService;
import com.rjkj.cf.bbibm.kjds.product.supplier.service.ProductVariantService;
import com.rjkj.cf.bbibm.kjds.product.supplier.service.SupplierProductService;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import com.rjkj.cf.common.security.service.RjkjUser;
import com.rjkj.cf.common.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * @描述：供应商发货记录
 * @项目：
 * @公司：软江科技
 * @作者：crq
 * @时间：2019-12-13 14:08:23
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/supplierproduct")
@Api(value = "supplierproduct", tags = "供应商发货记录管理")
public class SupplierProductController {

    private final SupplierProductService supplierProductService;
    private final RemoteProductFeignService remoteProductFeignService;
    private final ProductService productService;
    private final ProductVariantService productVariantService;

    /**
     * 分页查询
     *
     * @param page            分页对象
     * @param supplierProduct 供应商发货记录
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getSupplierProductPage(Page page, SupplierProduct supplierProduct) {
        RjkjUser user = SecurityUtils.getUser();
        return R.ok(supplierProductService.page(page, Wrappers.<SupplierProduct>query().lambda()
                .eq(SupplierProduct::getSupplierId, user.getId())));
    }


    /**
     * 通过id查询供应商发货记录
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") String id) {
        return R.ok(supplierProductService.getById(id));
    }

    /**
     * 新增供应商发货记录
     *
     * @param supplierProduct 供应商发货记录
     * @return R
     */
    @ApiOperation(value = "新增供应商发货记录", notes = "新增供应商发货记录")
    @SysLog("新增供应商发货记录")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('supplier_supplierproduct_add')")
    public R save(@RequestBody SupplierProduct supplierProduct) {
        return R.ok(supplierProductService.save(supplierProduct));
    }

    /**
     * 新增供应商发货记录
     *
     * @return R
     */
    @ApiOperation(value = "新增供应商待发货列表", notes = "新增供应商待发货列表")
    @SysLog("新增供应商待发货列表")
    @PostMapping("/saveSupplierProduct")
    public R saveSupplierProduct(@RequestBody OrderItemDetail orderItemDetail) {
        if (orderItemDetail == null) {
            throw new RuntimeException("参数为空！");
        }
        SupplierProduct supplierProduct = new SupplierProduct();
        String variationSku = orderItemDetail.getVariationSku();
        //根据变体SKU查询主商品信息
        Product product = remoteProductFeignService.getProductByVsku(variationSku).getData();
        if (product != null) {
            //查询变体信息
            ProductVariant productVariant = productVariantService.selectBySku(variationSku);
            supplierProduct.setId(IDUtils.getGUUID(""));
            supplierProduct.setProductId(product.getId());
            supplierProduct.setSupplierId(orderItemDetail.getSupplierUserId());
            supplierProduct.setProductSku(orderItemDetail.getVariationSku());
            supplierProduct.setProductName(product.getProductTitle());
            supplierProduct.setVariantColor(productVariant.getVariantColor());
            supplierProduct.setVariantSize(productVariant.getVariantSize());
            supplierProduct.setAmount(Integer.valueOf(orderItemDetail.getAmount()));
            supplierProduct.setStatus("1");
            supplierProduct.setImage(orderItemDetail.getImage());
            supplierProductService.save(supplierProduct);
            return R.ok(true,"已提交进货需求给商品供应商。");
        } else {
            return R.failed("未找到该商品的供货商！");
        }
//        return R.ok(true,"操作成功！");
    }

    /**
     * 修改供应商发货记录
     *
     * @param supplierProduct 供应商发货记录
     * @return R
     */
    @ApiOperation(value = "修改供应商发货记录", notes = "修改供应商发货记录")
    @SysLog("修改供应商发货记录")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('supplier_supplierproduct_edit')")
    public R updateById(@RequestBody SupplierProduct supplierProduct) {
        return R.ok(supplierProductService.updateById(supplierProduct));
    }

    /**
     * 通过id删除供应商发货记录
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除供应商发货记录", notes = "通过id删除供应商发货记录")
    @SysLog("通过id删除供应商发货记录")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('supplier_supplierproduct_del')")
    public R removeById(@PathVariable String id) {
        return R.ok(supplierProductService.removeById(id));
    }

}
