package com.rjkj.cf.bbibm.kjds.product.amazon.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rjkj.cf.bbibm.kjds.api.utils.KjdsUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 创建亚马逊上传信息xml文件
 *
 * @author EDZ
 */
public class CreateAmazonXmlUtil {


    /**
     * 创建亚马逊商品上传xml数据
     *
     * @throws DocumentException
     * @throws IOException
     */
    public String createProductXml(JSONArray arrays, String marktPlaceId) throws DocumentException, IOException {
        SAXReader reader = new SAXReader();

//		File file1 = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "amazon\\product.xml");
        InputStream inputStream = this.getClass().getResourceAsStream("/amazon/product.xml");

        Document read = reader.read(inputStream);
        Element document = read.getRootElement();//获取xml文件根节点
        Element messageTypeElement = document.element("MessageType");
        messageTypeElement.setText("Product");


        int messageId = 1;//定义xml文件中的messageid值(保证每个xml文件中messageid不重复)
        for (int f = 0; f < arrays.size(); f++) {

            JSONObject objd = JSONObject.parseObject(arrays.get(f).toString());
            String[] typed = objd.getString("type").split(",");//获取app分类亚马逊对应的分类信息

            JSONObject obj = JSONObject.parseObject(arrays.get(f).toString());
            JSONArray array = obj.getJSONArray("data");
            for (int i = 0; i < array.size(); i++) {
                JSONObject object = JSONObject.parseObject(array.get(i).toString());

                Element messageAll = document.addElement("Message");
                messageAll.addElement("MessageID").setText(messageId + "");
                messageAll.addElement("OperationType").setText("Update");
                Element productElement = messageAll.addElement("Product");

                productElement.addElement("SKU").setText(object.getString("sku"));

                Element standardProductIDElement = productElement.addElement("StandardProductID");
                standardProductIDElement.addElement("Type").setText("EAN");
                standardProductIDElement.addElement("Value").setText(object.getString("ean"));

                productElement.addElement("Condition").addElement("ConditionType").setText("New");

                Element descriptionDataElement = productElement.addElement("DescriptionData");
                String title = obj.getString("title");
                descriptionDataElement.addElement("Title").setText(KjdsUtils.capitalizationOfWordsText(title));
                descriptionDataElement.addElement("Brand").setText(obj.getString("brand"));
                descriptionDataElement.addElement("Description").setText(obj.getString("description"));
                String[] bulletPoints = obj.getString("bulletPoint").split("123msg123");
                if (bulletPoints.length > 5) {
                    for (int p = 0; p < 5; p++) {
                        String bulletPoint = bulletPoints[p];
                        if (StringUtils.isNotBlank(bulletPoint)) {
                            if (bulletPoint.length() > 500) {
                                String substring = bulletPoint.substring(0, 490).trim();
                                if (substring.startsWith("*")) {
                                    substring = bulletPoint.substring(1, 490);
                                }
                                descriptionDataElement.addElement("BulletPoint").setText(substring);
                            } else {
                                String point = bulletPoint.trim();
                                if (point.startsWith("*")) {
                                    point = bulletPoint.substring(1, point.length());
                                }
                                descriptionDataElement.addElement("BulletPoint").setText(point);
                            }
                        }
                    }
                } else {
                    for (int p = 0; p < bulletPoints.length; p++) {
                        String bulletPoint = bulletPoints[p];
                        if (StringUtils.isNotBlank(bulletPoint)) {
                            if (bulletPoint.length() > 500) {
                                String substring = bulletPoint.substring(0, 490).trim();
                                if (substring.startsWith("*")) {
                                    substring = bulletPoint.substring(1, 490);
                                }
                                descriptionDataElement.addElement("BulletPoint").setText(substring);
                            } else {
                                String bulletPointTrim = bulletPoint.trim();
                                if (bulletPointTrim.startsWith("*")) {
                                    bulletPointTrim = bulletPointTrim.substring(1, bulletPointTrim.length());
                                }
                                descriptionDataElement.addElement("BulletPoint").setText(bulletPointTrim);
                            }
                        }
                    }
                }
                descriptionDataElement.addElement("Manufacturer").setText(obj.getString("manufacturer"));

                //判断商品类型为汽车时需要填写MfrPartNumber字段
//				if("AutoAccessory".equals(typed[0])){
//					descriptionDataElement.addElement("MfrPartNumber").setText("19982020");
//				}
                descriptionDataElement.addElement("MfrPartNumber").setText("19982020");
                String[] searchTerms = obj.getString("searchTerms").split(",");

                if (searchTerms.length > 1) {
                    descriptionDataElement.addElement("SearchTerms").setText(searchTerms[0]);
                }

                //如果不为玩具分类时执行该代码
                if (!"ToysBaby".equals(typed[0])) {
                    if ("US".equals(marktPlaceId)) {//上传到美国
                        descriptionDataElement.addElement("ItemType").setText("2474987011");
                        descriptionDataElement.addElement("RecommendedBrowseNode").setText("2474987011");
                    } else if ("JP".equals(marktPlaceId)) {//上传到日本
                        descriptionDataElement.addElement("ItemType").setText("2474987011");
                        descriptionDataElement.addElement("RecommendedBrowseNode").setText("2474987011");
                    } else {
                        descriptionDataElement.addElement("ItemType").setText("2474987011");
                        descriptionDataElement.addElement("RecommendedBrowseNode").setText("2474987011");
                    }
                }


                Element productDataElement = messageAll.element("Product").addElement("ProductData");


                //生成home类型xml(家类型)
                if (StringUtils.equals(typed[0], "Home")) {

                    Element homeElement = productDataElement.addElement(typed[0]);
                    Element productTypeElement = homeElement.addElement("ProductType");
                    Element home2Element = productTypeElement.addElement(typed[1]);
                    home2Element.addElement("Material").setText("environmental protection");

                    Element variationDataElement = null;
                    //判断商品包含变体时
                    if (!StringUtils.equals(object.getString("par"), "single")) {
                        variationDataElement = home2Element.addElement("VariationData");
                    }

                    //上传数据为双变体时（size和color）
                    if ("".equals(obj.getString("size")) && "".equals(obj.getString("color"))) {
                        variationDataElement.addElement("VariationTheme").setText("Size-Color");
                        //上传数据为size单变体时
                    } else if ("".equals(obj.getString("size"))) {
                        variationDataElement.addElement("VariationTheme").setText("Size");
                        //上传数据为color单变体时
                    } else if ("".equals(obj.getString("color"))) {
                        variationDataElement.addElement("VariationTheme").setText("Color");
                    }


                    //判断为父类时
                    if (StringUtils.equals(object.getString("par"), "parent")) {
                        homeElement.addElement("Parentage").setText(object.getString("par"));
                    } else if (StringUtils.equals(object.getString("par"), "child")) {
                        //上传数据为双变体时（size和color）
                        if ("".equals(obj.getString("size")) && "".equals(obj.getString("color"))) {
                            variationDataElement.addElement("Size").setText(object.getString("size"));
                            variationDataElement.addElement("Color").setText(object.getString("color"));
                            //上传数据为size单变体时
                        } else if ("".equals(obj.getString("size"))) {
                            variationDataElement.addElement("Size").setText(object.getString("size"));
                            //上传数据为color单变体时
                        } else if ("".equals(obj.getString("color"))) {
                            variationDataElement.addElement("Color").setText(object.getString("color"));
                        }

                        homeElement.addElement("Parentage").setText(object.getString("par"));

                    }

                }


                //生成Clothing类型xml(衣服类型)
                if (StringUtils.equals(typed[0], "Clothing")) {

                    Element clothingDataElement = productDataElement.addElement(typed[0]);

                    Element variationDataElement = null;
                    //判断商品包含变体信息时
                    if (!StringUtils.equals(object.getString("par"), "single")) {
                        variationDataElement = clothingDataElement.addElement("VariationData");
                        variationDataElement.addElement("Parentage").setText(object.getString("par"));
                    } else if (StringUtils.equals(object.getString("par"), "single")) {
                        if ("Hat".equals(typed[2])) {
                            variationDataElement = clothingDataElement.addElement("VariationData");
//                            variationDataElement.addElement("Parentage").setText("parent");
                        }

                    }


                    //判断为子类（变体时）
                    if (StringUtils.equals(object.getString("par"), "child")) {

                        //上传数据为双变体时（size和color）
                        if ("".equals(obj.getString("size")) && "".equals(obj.getString("color"))) {

                            variationDataElement.addElement("Size").setText(object.getString("size"));
                            variationDataElement.addElement("Color").setText(object.getString("color"));

                            //上传数据为size单变体时
                        } else if ("".equals(obj.getString("size"))) {
                            variationDataElement.addElement("Size").setText(object.getString("size"));

                            //上传数据为color单变体时
                        } else if ("".equals(obj.getString("color"))) {
                            //为帽子的时候没有尺寸需要填写size信息
                            if ("Hat".equals(typed[2])) {
                                variationDataElement.addElement("Size").setText("58cm");
                            }
                            variationDataElement.addElement("Color").setText(object.getString("color"));

                        }
                    }


                    //判断商品包含变体信息时
                    if (!StringUtils.equals(object.getString("par"), "single")) {
                        //上传数据为双变体时（size和color）
                        if ("".equals(obj.getString("size")) && "".equals(obj.getString("color"))) {
                            variationDataElement.addElement("VariationTheme").setText("SizeColor");
                            //上传数据为size单变体时
                        } else if ("".equals(obj.getString("size"))) {
                            variationDataElement.addElement("VariationTheme").setText("Size");
                            //上传数据为color单变体时
                        } else if ("".equals(obj.getString("color"))) {
                            variationDataElement.addElement("VariationTheme").setText("Color");
                        }
                    }


                    //当帽子没有变体的时候需要设置Size属性
                    if (StringUtils.equals(object.getString("par"), "single")) {
                        if ("Hat".equals(typed[2])) {
                            variationDataElement.addElement("Size").setText("58cm");
                        }
                    }

                    Element classificationDataElement = clothingDataElement.addElement("ClassificationData");
                    classificationDataElement.addElement("ClothingType").setText(typed[2]);
                    classificationDataElement.addElement("Department").setText(typed[1]);

                }


                //生成baby类型xml(婴儿类型)
                if (StringUtils.equals(typed[0], "Baby")) {

                    Element babyDataElement = productDataElement.addElement(typed[0]);
                    Element productTypeElement = babyDataElement.addElement("ProductType");
                    Element typesElement = productTypeElement.addElement(typed[1]);

                    //判断为子类（变体时）
                    if (StringUtils.equals(object.getString("par"), "child")) {


                        //上传数据为双变体时（size和color）
                        if ("".equals(obj.getString("size")) && "".equals(obj.getString("color"))) {

                            typesElement.addElement("ColorName").setText(object.getString("color"));
                            Element variationDataElement = typesElement.addElement("VariationData");
                            variationDataElement.addElement("Parentage").setText(object.getString("par"));
                            variationDataElement.addElement("VariationTheme").setText("SizeName-ColorName");
                            typesElement.addElement("SizeName").setText(object.getString("size"));
                            //上传数据为size单变体时
                        } else if ("".equals(obj.getString("size"))) {

                            Element variationDataElement = typesElement.addElement("VariationData");
                            variationDataElement.addElement("Parentage").setText(object.getString("par"));
                            variationDataElement.addElement("VariationTheme").setText("SizeName");
                            typesElement.addElement("SizeName").setText(object.getString("size"));
                            //上传数据为color单变体时
                        } else if ("".equals(obj.getString("color"))) {

                            typesElement.addElement("ColorName").setText(object.getString("color"));
                            Element variationDataElement = typesElement.addElement("VariationData");
                            variationDataElement.addElement("Parentage").setText(object.getString("par"));
                            variationDataElement.addElement("VariationTheme").setText("ColorName");

                        }


                    } else if (StringUtils.equals(object.getString("par"), "parent")) {

                        Element variationDataElement = typesElement.addElement("VariationData");
                        variationDataElement.addElement("Parentage").setText(object.getString("par"));

                        //上传数据为双变体时（size和color）
                        if ("".equals(obj.getString("size")) && "".equals(obj.getString("color"))) {
                            variationDataElement.addElement("VariationTheme").setText("SizeName-ColorName");
                            //上传数据为size单变体时
                        } else if ("".equals(obj.getString("size"))) {
                            variationDataElement.addElement("VariationTheme").setText("SizeName");
                            //上传数据为color单变体时
                        } else if ("".equals(obj.getString("color"))) {
                            variationDataElement.addElement("VariationTheme").setText("ColorName");
                        }


                    }
                    typesElement.addElement("UnitCount").addAttribute("unitOfMeasure", "Count").setText("1");

                }


                //生成Beauty类型xml(美妆类型)
                if (StringUtils.equals(typed[0], "Beauty")) {
                    Element beautyDataElement = productDataElement.addElement(typed[0]);
                    Element productTypeElement = beautyDataElement.addElement("ProductType");
                    Element typesElement = productTypeElement.addElement(typed[1]);

                    Element variationDataElement = null;
                    //判断商品包含变体的时候
                    if (!StringUtils.equals(object.getString("par"), "single")) {
                        variationDataElement = typesElement.addElement("VariationData");
                        variationDataElement.addElement("Parentage").setText(object.getString("par"));
                    }


                    //上传数据为双变体时（size和color）
                    if ("".equals(obj.getString("size")) && "".equals(obj.getString("color"))) {
                        variationDataElement.addElement("VariationTheme").setText("Size-Color");
                        //上传数据为size单变体时
                    } else if ("".equals(obj.getString("size"))) {
                        variationDataElement.addElement("VariationTheme").setText("Size");
                        //上传数据为color单变体时
                    } else if ("".equals(obj.getString("color"))) {
                        variationDataElement.addElement("VariationTheme").setText("Color");
                    }

                    //判断为子类（变体时）
                    if (StringUtils.equals(object.getString("par"), "child")) {

                        //上传数据为双变体时（size和color）
                        if ("".equals(obj.getString("size")) && "".equals(obj.getString("color"))) {
                            variationDataElement.addElement("Size").setText(object.getString("size"));
                            variationDataElement.addElement("Color").setText(object.getString("color"));
                            //上传数据为size单变体时
                        } else if ("".equals(obj.getString("size"))) {
                            variationDataElement.addElement("Size").setText(object.getString("size"));
                            //上传数据为color单变体时
                        } else if ("".equals(obj.getString("color"))) {
                            variationDataElement.addElement("Color").setText(object.getString("color"));
                        }

                    }
                    typesElement.addElement("UnitCount").addAttribute("unitOfMeasure", "Count").setText("1");

                }


                //生成health类型xml(健康类型)
                if (StringUtils.equals(typed[0], "Health")) {
                    Element healthDataElement = productDataElement.addElement("Health");
                    Element productTypeElement = healthDataElement.addElement("ProductType");
                    Element typesElement = productTypeElement.addElement(typed[1]);

                    Element variationDataElement = null;
                    //判断商品包含变体的时候
                    if (!StringUtils.equals(object.getString("par"), "single")) {
                        variationDataElement = typesElement.addElement("VariationData");
                        variationDataElement.addElement("Parentage").setText(object.getString("par"));
                    }

                    //上传数据为双变体时（size和color）
                    if ("".equals(obj.getString("size")) && "".equals(obj.getString("color"))) {
                        variationDataElement.addElement("VariationTheme").setText("Size-Color");
                        //上传数据为size单变体时
                    } else if ("".equals(obj.getString("size"))) {
                        variationDataElement.addElement("VariationTheme").setText("Size");
                        //上传数据为color单变体时
                    } else if ("".equals(obj.getString("color"))) {
                        variationDataElement.addElement("VariationTheme").setText("Color");
                    }

                    //判断为子类（变体时）
                    if (StringUtils.equals(object.getString("par"), "child")) {

                        //上传数据为双变体时（size和color）
                        if ("".equals(obj.getString("size")) && "".equals(obj.getString("color"))) {
                            variationDataElement.addElement("Size").setText(object.getString("size"));
                            variationDataElement.addElement("Color").setText(object.getString("color"));
                            //上传数据为size单变体时
                        } else if ("".equals(obj.getString("size"))) {
                            variationDataElement.addElement("Size").setText(object.getString("size"));
                            //上传数据为color单变体时
                        } else if ("".equals(obj.getString("color"))) {
                            variationDataElement.addElement("Color").setText(object.getString("color"));
                        }
                    }
                    typesElement.addElement("UnitCount").addAttribute("unitOfMeasure", "Count").setText("1");

                }


                //生成Shoes类型xml(鞋子类型)
                if (StringUtils.equals(typed[0], "Shoes")) {
                    Element shoesDataElement = productDataElement.addElement(typed[0]);
                    shoesDataElement.addElement("ClothingType").setText(typed[2]);
                    if (StringUtils.equals(object.getString("par"), "child")) {
                        Element variationDataElement = shoesDataElement.addElement("VariationData");
                        variationDataElement.addElement("Parentage").setText(object.getString("par"));


                        //上传数据为双变体时（size和color）
                        if ("".equals(obj.getString("size")) && "".equals(obj.getString("color"))) {
                            variationDataElement.addElement("Size").setText(object.getString("size"));
                            variationDataElement.addElement("Color").setText(object.getString("color"));
                            variationDataElement.addElement("VariationTheme").setText("SizeColor");
                            //上传数据为size单变体时
                        } else if ("".equals(obj.getString("size"))) {
                            variationDataElement.addElement("Size").setText(object.getString("size"));
                            variationDataElement.addElement("VariationTheme").setText("Size");
                            //上传数据为color单变体时
                        } else if ("".equals(obj.getString("color"))) {
                            variationDataElement.addElement("Color").setText(object.getString("color"));
                            variationDataElement.addElement("VariationTheme").setText("Color");
                        }


                        Element classificationDataElement = shoesDataElement.addElement("ClassificationData");
                        classificationDataElement.addElement("Department").setText(typed[1]);

                        //根据分类性别设置TargetGender
                        if (StringUtils.equals(typed[1], "mens")) {
                            classificationDataElement.addElement("TargetGender").setText("male");
                        } else if (StringUtils.equals(typed[1], "womens")) {
                            classificationDataElement.addElement("TargetGender").setText("female");
                        } else {
                            classificationDataElement.addElement("TargetGender").setText("unisex");
                        }


                        //判断该分类信息为Shoes的时候
                        if (StringUtils.equals(typed[2], "Shoes")) {
                            Element shoeSizeComplianceDataElement = shoesDataElement.addElement("ShoeSizeComplianceData");
                            shoeSizeComplianceDataElement.addElement("AgeRangeDescription").setText("adult");
                            shoeSizeComplianceDataElement.addElement("FootwearSizeSystem").setText("us_footwear_size_system");
                            if (StringUtils.equals(typed[1], "boys")) {
                                shoeSizeComplianceDataElement.addElement("ShoeSizeAgeGroup").setText("big_kid");
                                shoeSizeComplianceDataElement.addElement("ShoeSizeGender").setText("men");
                            } else if (StringUtils.equals(typed[1], "mens")) {
                                shoeSizeComplianceDataElement.addElement("ShoeSizeAgeGroup").setText("adult");
                                shoeSizeComplianceDataElement.addElement("ShoeSizeGender").setText("men");
                            } else {
                                shoeSizeComplianceDataElement.addElement("ShoeSizeAgeGroup").setText("adult");
                                shoeSizeComplianceDataElement.addElement("ShoeSizeGender").setText("women");
                            }
                            shoeSizeComplianceDataElement.addElement("ShoeSizeClass").setText("numeric_range");
                            shoeSizeComplianceDataElement.addElement("ShoeSizeWidth").setText("medium");


                            //将中国的码数转换为xsd中的枚举类型码数
                            if (StringUtils.isNotEmpty(object.getString("size"))) {
                                //欧洲码数
                                BigDecimal ouZhouSize = new BigDecimal(object.getString("size"));
                                //中国码数
                                BigDecimal addAll = ouZhouSize.add(new BigDecimal(10));
                                BigDecimal chinaSize = addAll.multiply(new BigDecimal(5));
                                BigDecimal divide = chinaSize.divide(new BigDecimal(10), 1, BigDecimal.ROUND_HALF_UP);
                                //转换为美国码
                                double usSize = divide.subtract(new BigDecimal(18)).add(new BigDecimal(0.5)).doubleValue();
                                String lastUsSize = String.valueOf(usSize);
                                String[] splitValue = lastUsSize.split("\\.");
                                String statedOne = "";
                                String statedTwo = "";
                                if ("0".equals(splitValue[1])) {
                                    statedOne = "numeric_" + splitValue[0];
                                    statedTwo = "numeric_" + splitValue[0] + "_point_5";
                                } else {
                                    statedOne = "numeric_" + splitValue[0] + "_point_5";
                                    Integer integerTwo = Integer.valueOf(splitValue[0]) + 1;
                                    statedTwo = "numeric_" + integerTwo;
                                }
                                shoeSizeComplianceDataElement.addElement("ShoeSize").setText(statedOne);
                                shoeSizeComplianceDataElement.addElement("ShoeSizeToRange").setText(statedTwo);
                            }

                        }


                    } else {
                        Element variationDataElement = shoesDataElement.addElement("VariationData");
                        variationDataElement.addElement("Parentage").setText(object.getString("par"));

                        //上传数据为双变体时（size和color）
                        if ("".equals(obj.getString("size")) && "".equals(obj.getString("color"))) {
                            variationDataElement.addElement("VariationTheme").setText("SizeColor");

                            //上传数据为size单变体时
                        } else if ("".equals(obj.getString("size"))) {
                            variationDataElement.addElement("VariationTheme").setText("Size");

                            //上传数据为color单变体时
                        } else if ("".equals(obj.getString("color"))) {
                            variationDataElement.addElement("VariationTheme").setText("Color");

                        }

                        Element classificationDataElement = shoesDataElement.addElement("ClassificationData");
                        classificationDataElement.addElement("Department").setText(typed[1]);

                        //根据分类性别设置TargetGender
                        if (StringUtils.equals(typed[1], "mens")) {
                            classificationDataElement.addElement("TargetGender").setText("male");
                        } else if (StringUtils.equals(typed[1], "womens")) {
                            classificationDataElement.addElement("TargetGender").setText("female");
                        } else {
                            classificationDataElement.addElement("TargetGender").setText("unisex");
                        }

                    }


                }


                //生成Sports类型xml(运动类型)
                if (StringUtils.equals(typed[0], "Sports")) {
                    Element sportsDataElement = productDataElement.addElement(typed[0]);
                    sportsDataElement.addElement("ProductType").setText(typed[2]);
                    Element variationDataElement = sportsDataElement.addElement("VariationData");
                    if (StringUtils.equals(object.getString("par"), "child")) {

                        //上传数据为双变体时（size和color）
                        if ("".equals(obj.getString("size")) && "".equals(obj.getString("color"))) {
                            variationDataElement.addElement("Parentage").setText(object.getString("par"));
                            variationDataElement.addElement("VariationTheme").setText("ColorSize");
                            variationDataElement.addElement("Color").setText(object.getString("color"));
                            variationDataElement.addElement("Department").setText(typed[1]);
                            variationDataElement.addElement("Material").setText("Comfortable");
                            variationDataElement.addElement("Size").setText(object.getString("size"));

                            //上传数据为size单变体时
                        } else if ("".equals(obj.getString("size"))) {
                            variationDataElement.addElement("Parentage").setText(object.getString("par"));
                            variationDataElement.addElement("VariationTheme").setText("Size");
                            variationDataElement.addElement("Department").setText(typed[1]);
                            variationDataElement.addElement("Material").setText("Comfortable");
                            variationDataElement.addElement("Size").setText(object.getString("size"));

                            //上传数据为color单变体时
                        } else if ("".equals(obj.getString("color"))) {
                            variationDataElement.addElement("Parentage").setText(object.getString("par"));
                            variationDataElement.addElement("VariationTheme").setText("Color");
                            variationDataElement.addElement("Color").setText(object.getString("color"));
                            variationDataElement.addElement("Department").setText(typed[1]);
                            variationDataElement.addElement("Material").setText("Comfortable");

                        }


                    } else if (StringUtils.equals(object.getString("par"), "parent")) {

                        //上传数据为双变体时（size和color）
                        if ("".equals(obj.getString("size")) && "".equals(obj.getString("color"))) {
                            variationDataElement.addElement("Parentage").setText(object.getString("par"));
                            variationDataElement.addElement("VariationTheme").setText("ColorSize");
                            variationDataElement.addElement("Department").setText(typed[1]);
                            variationDataElement.addElement("Material").setText("Comfortable");

                            //上传数据为size单变体时
                        } else if ("".equals(obj.getString("size"))) {
                            variationDataElement.addElement("Parentage").setText(object.getString("par"));
                            variationDataElement.addElement("VariationTheme").setText("Size");
                            variationDataElement.addElement("Department").setText(typed[1]);
                            variationDataElement.addElement("Material").setText("Comfortable");

                            //上传数据为color单变体时
                        } else if ("".equals(obj.getString("color"))) {
                            variationDataElement.addElement("Parentage").setText(object.getString("par"));
                            variationDataElement.addElement("VariationTheme").setText("Color");
                            variationDataElement.addElement("Department").setText(typed[1]);
                            variationDataElement.addElement("Material").setText("Comfortable");

                        }

                    } else {
                        variationDataElement.addElement("Material").setText("Comfortable");

                    }
                }


                //生成ToysBaby类型xml(小孩玩具类型)
                if (StringUtils.equals(typed[0], "ToysBaby")) {

                    if ("US".equals(marktPlaceId)) {//上传到美国
                        descriptionDataElement.addElement("ItemType").setText("2474987011");
                        descriptionDataElement.addElement("TargetAudience").setText("boys");
                        descriptionDataElement.addElement("RecommendedBrowseNode").setText("2474987011");
                    } else if ("JP".equals(marktPlaceId)) {//上传到日本
                        descriptionDataElement.addElement("ItemType").setText("2474987011");
                        descriptionDataElement.addElement("TargetAudience").setText("boys");
                        descriptionDataElement.addElement("RecommendedBrowseNode").setText("2474987011");
                    } else {
                        descriptionDataElement.addElement("ItemType").setText("2474987011");
                        descriptionDataElement.addElement("TargetAudience").setText("boys");
                        descriptionDataElement.addElement("RecommendedBrowseNode").setText("2474987011");
                    }

                    Element toysBabyDataElement = productDataElement.addElement(typed[0]);
                    toysBabyDataElement.addElement("ProductType").setText(typed[1]);
                    Element ageRecommendationElement = toysBabyDataElement.addElement("AgeRecommendation");
                    ageRecommendationElement.addElement("MinimumManufacturerAgeRecommended").addAttribute("unitOfMeasure", "years").setText("5");
                    ageRecommendationElement.addElement("MaximumManufacturerAgeRecommended").addAttribute("unitOfMeasure", "years").setText("10");


                    Element variationDataElement = null;
                    //判断商品没有变体的时候
                    if (!StringUtils.equals(object.getString("par"), "single")) {
                        variationDataElement = toysBabyDataElement.addElement("VariationData");
                        variationDataElement.addElement("Parentage").setText(object.getString("par"));
                    }

                    //上传数据为双变体时（size和color）
                    if ("".equals(obj.getString("size")) && "".equals(obj.getString("color"))) {
                        variationDataElement.addElement("VariationTheme").setText("Size-Color");
                        //上传数据为size单变体时
                    } else if ("".equals(obj.getString("size"))) {
                        variationDataElement.addElement("VariationTheme").setText("Size");
                        //上传数据为color单变体时
                    } else if ("".equals(obj.getString("color"))) {
                        variationDataElement.addElement("VariationTheme").setText("Color");
                    }

                    if (StringUtils.equals(object.getString("par"), "child")) {

                        //上传数据为双变体时（size和color）
                        if ("".equals(obj.getString("size")) && "".equals(obj.getString("color"))) {
                            toysBabyDataElement.addElement("Size").setText(object.getString("size"));
                            toysBabyDataElement.addElement("Color").setText(object.getString("color"));
                            //上传数据为size单变体时
                        } else if ("".equals(obj.getString("size"))) {
                            toysBabyDataElement.addElement("Size").setText(object.getString("size"));
                            //上传数据为color单变体时
                        } else if ("".equals(obj.getString("color"))) {
                            toysBabyDataElement.addElement("Color").setText(object.getString("color"));
                        }


                    }

                }


                //生成Jewelry类型xml(珠宝类型)
                if (StringUtils.equals(typed[0], "Jewelry")) {
                    Element jewelryDataElement = productDataElement.addElement(typed[0]);
                    Element productTypeElement = jewelryDataElement.addElement("ProductType");
                    Element typesElement = productTypeElement.addElement(typed[2]);
                    if (StringUtils.equals(object.getString("par"), "child")) {
                        Element variationDataElement = typesElement.addElement("VariationData");
                        variationDataElement.addElement("Parentage").setText(object.getString("par"));
                        variationDataElement.addElement("VariationTheme").setText("Color");
                        variationDataElement.addElement("MetalType").setText("base");
                        typesElement.addElement("Material").setText("Boutique");
                        typesElement.addElement("Stone").addElement("GemType").setText("NA");
                        typesElement.addElement("DepartmentName").setText(typed[1]);
                        jewelryDataElement.addElement("Color").setText(object.getString("color"));
                    } else if (StringUtils.equals(object.getString("par"), "parent")) {
                        Element variationDataElement = typesElement.addElement("VariationData");
                        variationDataElement.addElement("Parentage").setText(object.getString("par"));
                        variationDataElement.addElement("VariationTheme").setText("Color");
                        variationDataElement.addElement("MetalType").setText("base");
                        typesElement.addElement("Material").setText("Boutique");
                        typesElement.addElement("Stone").addElement("GemType").setText("NA");
                        typesElement.addElement("DepartmentName").setText(typed[1]);
                    } else {
                        Element variationDataElement = typesElement.addElement("VariationData");
                        variationDataElement.addElement("Parentage").setText("parent");
                        variationDataElement.addElement("VariationTheme").setText("MetalType");
                        variationDataElement.addElement("MetalType").setText("base");
                        typesElement.addElement("Material").setText("Boutique");
                        typesElement.addElement("Stone").addElement("GemType").setText("NA");
                        typesElement.addElement("DepartmentName").setText(typed[1]);
                    }


                }

                //生成PetSupplies类型xml(宠物类型)
                if (StringUtils.equals(typed[0], "PetSupplies")) {

                    Element petSuppliesDataElement = productDataElement.addElement(typed[0]);
                    Element productTypeElement = petSuppliesDataElement.addElement("ProductType");
                    Element typesElement = productTypeElement.addElement(typed[1]);

                    //判断是否为子变体数据
                    if (StringUtils.equals(object.getString("par"), "child")) {

                        //上传数据为双变体时（size和color）
                        if ("".equals(obj.getString("size")) && "".equals(obj.getString("color"))) {
                            Element variationDataElement = typesElement.addElement("VariationData");
                            variationDataElement.addElement("Parentage").setText(object.getString("par"));
                            variationDataElement.addElement("VariationTheme").setText("SizeColor");
                            Element colorSpecificationElement = typesElement.addElement("ColorSpecification");
                            colorSpecificationElement.addElement("Color").setText(object.getString("color"));
                            colorSpecificationElement.addElement("ColorMap").setText(object.getString("color"));
                            typesElement.addElement("Size").setText(object.getString("size"));
                            //上传数据为size单变体时
                        } else if ("".equals(obj.getString("size"))) {
                            Element variationDataElement = typesElement.addElement("VariationData");
                            variationDataElement.addElement("Parentage").setText(object.getString("par"));
                            variationDataElement.addElement("VariationTheme").setText("Size");
                            typesElement.addElement("Size").setText(object.getString("size"));
                            //上传数据为color单变体时
                        } else if ("".equals(obj.getString("color"))) {
                            Element variationDataElement = typesElement.addElement("VariationData");
                            variationDataElement.addElement("Parentage").setText(object.getString("par"));
                            variationDataElement.addElement("VariationTheme").setText("Color");
                            Element colorSpecificationElement = typesElement.addElement("ColorSpecification");
                            colorSpecificationElement.addElement("Color").setText(object.getString("color"));
                            colorSpecificationElement.addElement("ColorMap").setText(object.getString("color"));

                        }

                    } else if (StringUtils.equals(object.getString("par"), "parent")) {

                        //上传数据为双变体时（size和color）
                        if ("".equals(obj.getString("size")) && "".equals(obj.getString("color"))) {
                            Element variationDataElement = typesElement.addElement("VariationData");
                            variationDataElement.addElement("Parentage").setText(object.getString("par"));
                            variationDataElement.addElement("VariationTheme").setText("SizeColor");
                            //上传数据为size单变体时
                        } else if ("".equals(obj.getString("size"))) {
                            Element variationDataElement = typesElement.addElement("VariationData");
                            variationDataElement.addElement("Parentage").setText(object.getString("par"));
                            variationDataElement.addElement("VariationTheme").setText("Size");
                            //上传数据为color单变体时
                        } else if ("".equals(obj.getString("color"))) {
                            Element variationDataElement = typesElement.addElement("VariationData");
                            variationDataElement.addElement("Parentage").setText(object.getString("par"));
                            variationDataElement.addElement("VariationTheme").setText("Color");
                        }
                        //判断为不存在变体时作为独立商品上传
                    } else if (StringUtils.equals(object.getString("par"), "single")) {
                        typesElement.addElement("Material").setText("cotton");
                    }
                }
                //生成CE（3C数码类型）类型xml
                if (StringUtils.equals(typed[0], "CE")) {
                    Element ceDataElement = productDataElement.addElement(typed[0]);
                    Element productTypeElement = ceDataElement.addElement("ProductType");
                    Element typesElement = productTypeElement.addElement(typed[1]);
                    //判断是否是变体数据
                    if (StringUtils.equals(object.getString("par"), "child")) {
                        //上传数据为双变体时（size和color）
                        if ("".equals(obj.getString("size")) && "".equals(obj.getString("color"))) {
                            Element variationDataElement = typesElement.addElement("VariationData");
                            variationDataElement.addElement("Parentage").setText(object.getString("par"));
                            variationDataElement.addElement("VariationTheme").setText("Size-Color");
                            ceDataElement.addElement("Color").setText(object.getString("color"));
                            ceDataElement.addElement("Size").setText(object.getString("size"));

                            //上传数据为size单变体时
                        } else if ("".equals(obj.getString("size"))) {
                            Element variationDataElement = typesElement.addElement("VariationData");
                            variationDataElement.addElement("Parentage").setText(object.getString("par"));
                            variationDataElement.addElement("VariationTheme").setText("Size");
                            ceDataElement.addElement("Size").setText(object.getString("size"));

                            //上传数据为color单变体时
                        } else if ("".equals(obj.getString("color"))) {
                            Element variationDataElement = typesElement.addElement("VariationData");
                            variationDataElement.addElement("Parentage").setText(object.getString("par"));
                            variationDataElement.addElement("VariationTheme").setText("Color");
                            ceDataElement.addElement("Color").setText(object.getString("color"));

                        }

                        //判断商品信息为parent时
                    } else if (StringUtils.equals(object.getString("par"), "parent")) {

                        //上传数据为双变体时（size和color）
                        if ("".equals(obj.getString("size")) && "".equals(obj.getString("color"))) {
                            Element variationDataElement = typesElement.addElement("VariationData");
                            variationDataElement.addElement("Parentage").setText(object.getString("par"));
                            variationDataElement.addElement("VariationTheme").setText("Size-Color");

                            //上传数据为size单变体时
                        } else if ("".equals(obj.getString("size"))) {
                            Element variationDataElement = typesElement.addElement("VariationData");
                            variationDataElement.addElement("Parentage").setText(object.getString("par"));
                            variationDataElement.addElement("VariationTheme").setText("Size");

                            //上传数据为color单变体时
                        } else if ("".equals(obj.getString("color"))) {
                            Element variationDataElement = typesElement.addElement("VariationData");
                            variationDataElement.addElement("Parentage").setText(object.getString("par"));
                            variationDataElement.addElement("VariationTheme").setText("Color");
                        }

                    }

                    typesElement.addElement("ItemPackageQuantity").setText("1");

                }


                //生成AutoAccessory类型数据（汽车配件）
                if (StringUtils.equals(typed[0], "AutoAccessory")) {
                    Element ceDataElement = productDataElement.addElement(typed[0]);
                    Element productTypeElement = ceDataElement.addElement("ProductType");
                    Element typesElement = productTypeElement.addElement(typed[1]);


                    //判断是否为父级
                    if (StringUtils.equals(object.getString("par"), "parent")) {

                        //上传数据为双变体时（size和color）
                        if ("".equals(obj.getString("size")) && "".equals(obj.getString("color"))) {
                            Element variationDataElement = typesElement.addElement("VariationData");
                            variationDataElement.addElement("Parentage").setText("parent");
                            variationDataElement.addElement("VariationTheme").setText("Size-Color");

                            //上传数据为size单变体时
                        } else if ("".equals(obj.getString("size"))) {
                            Element variationDataElement = typesElement.addElement("VariationData");
                            variationDataElement.addElement("Parentage").setText("parent");
                            variationDataElement.addElement("VariationTheme").setText("Size");


                            //上传数据为color单变体时
                        } else if ("".equals(obj.getString("color"))) {
                            Element variationDataElement = typesElement.addElement("VariationData");
                            variationDataElement.addElement("Parentage").setText("parent");
                            variationDataElement.addElement("VariationTheme").setText("Color");

                        }


                        //判断是否为变体
                    } else if (StringUtils.equals(object.getString("par"), "child")) {

                        //上传数据为双变体时（size和color）
                        if ("".equals(obj.getString("size")) && "".equals(obj.getString("color"))) {
                            Element variationDataElement = typesElement.addElement("VariationData");
                            variationDataElement.addElement("Parentage").setText("child");
                            variationDataElement.addElement("VariationTheme").setText("Size-Color");
                            Element colorSpecificationElement = typesElement.addElement("ColorSpecification");
                            colorSpecificationElement.addElement("Color").setText(object.getString("color"));
                            colorSpecificationElement.addElement("ColorMap").setText(object.getString("color"));
                            typesElement.addElement("Size").setText(object.getString("size"));

                            //上传数据为size单变体时
                        } else if ("".equals(obj.getString("size"))) {
                            Element variationDataElement = typesElement.addElement("VariationData");
                            variationDataElement.addElement("Parentage").setText("child");
                            variationDataElement.addElement("VariationTheme").setText("Size");
                            typesElement.addElement("Size").setText(object.getString("size"));


                            //上传数据为color单变体时
                        } else if ("".equals(obj.getString("color"))) {
                            Element variationDataElement = typesElement.addElement("VariationData");
                            variationDataElement.addElement("Parentage").setText("child");
                            variationDataElement.addElement("VariationTheme").setText("Color");
                            Element colorSpecificationElement = typesElement.addElement("ColorSpecification");
                            colorSpecificationElement.addElement("Color").setText(object.getString("color"));
                            colorSpecificationElement.addElement("ColorMap").setText(object.getString("color"));

                        }


                        //判断是否不存在变体的情况
                    } else if (StringUtils.equals(object.getString("par"), "single")) {

                        typesElement.addElement("Material").setText("environmental protection");

                    }

                    ceDataElement.addElement("OEMPartNumber").setText("19982020");


                }


//				//生成CameraPhoto类型数据(相机相关数据)
//				if(StringUtils.equals(typed[0], "CameraPhoto")) {
//					Element cameraPhotoDataElement = productDataElement.addElement(typed[0]);
//					Element productTypeElement = cameraPhotoDataElement.addElement("ProductType");
//					Element typesElement = productTypeElement.addElement(typed[1]);
//
//					if("OtherAccessory".equals(typed[1])){
//						typesElement.addElement("CameraAccessories").setText("close-up-accessories");
//					}
//
//					cameraPhotoDataElement.addElement("BatteryCellType").setText("lithium");
//					cameraPhotoDataElement.addElement("MfrPartNumber").setText("TXQPSOAS");
//
//					//判断是否是变体数据
//					if(StringUtils.equals(object.getString("par"), "child")) {
//
//						//上传数据为双变体时（size和color）
//						if("".equals(obj.getString("size")) && "".equals(obj.getString("color"))) {
//							cameraPhotoDataElement.addElement("Color").setText(object.getString("color"));
//							cameraPhotoDataElement.addElement("ColorMap").setText(object.getString("color"));
//							cameraPhotoDataElement.addElement("Parentage").setText(object.getString("par"));
//							cameraPhotoDataElement.addElement("VariationTheme").setText("SizeName-ColorName-CustomerPackageType");
//							cameraPhotoDataElement.addElement("SizeName").setText(object.getString("size"));
//							//上传数据为size单变体时
//						}else if("".equals(obj.getString("size"))){
//							cameraPhotoDataElement.addElement("Parentage").setText(object.getString("par"));
//							cameraPhotoDataElement.addElement("VariationTheme").setText("SizeName-CustomerPackageType");
//							cameraPhotoDataElement.addElement("SizeName").setText(object.getString("size"));
//							//上传数据为color单变体时
//						}else if("".equals(obj.getString("color"))){
//							cameraPhotoDataElement.addElement("Color").setText(object.getString("color"));
//							cameraPhotoDataElement.addElement("ColorMap").setText(object.getString("color"));
//							cameraPhotoDataElement.addElement("Parentage").setText(object.getString("par"));
//							cameraPhotoDataElement.addElement("VariationTheme").setText("ColorName-CustomerPackageType");
//
//						}
//
//						//判断为parent的时候
//					}else if(StringUtils.equals(object.getString("par"), "parent")){
//							//上传数据为双变体时（size和color）
//						if("".equals(obj.getString("size")) && "".equals(obj.getString("color"))) {
//							cameraPhotoDataElement.addElement("Parentage").setText(object.getString("par"));
//							cameraPhotoDataElement.addElement("VariationTheme").setText("SizeName-ColorName-CustomerPackageType");
//							//上传数据为size单变体时
//						}else if("".equals(obj.getString("size"))){
//							cameraPhotoDataElement.addElement("Parentage").setText(object.getString("par"));
//							cameraPhotoDataElement.addElement("VariationTheme").setText("SizeName-CustomerPackageType");
//							//上传数据为color单变体时
//						}else if("".equals(obj.getString("color"))){
//							cameraPhotoDataElement.addElement("Parentage").setText(object.getString("par"));
//							cameraPhotoDataElement.addElement("VariationTheme").setText("ColorName-CustomerPackageType");
//
//						}
//
//					}
//
//				}


                messageId++;
            }

        }

//TODO
        createFilePath();

        Date dd = new Date();
        String name = dd.getTime() + "shangpin";
        //TODO
        return createXmlFile(read, name);
    }


    /**
     * 创建亚马逊商品关联关系xml
     *
     * @throws DocumentException
     * @throws IOException
     */
    public String createRelationXml(JSONArray arrays) throws DocumentException, IOException {
//		File file1 = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "amazon\\relation.xml");
        InputStream inputStream = this.getClass().getResourceAsStream("/amazon/relation.xml");


        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(inputStream);
        Element rootElement = document.getRootElement();

        //定义一个messageid
        int messageId = 1;
        for (int k = 0; k < arrays.size(); k++) {
            JSONObject objct = JSONObject.parseObject(arrays.get(k).toString());
            JSONArray array = objct.getJSONArray("data");

            //获取data下的第一级数据用于判断商品是否包含变体
            JSONObject objectState = JSONObject.parseObject(array.get(0).toString());

            //判断当前商品有变体的时候
            if (!StringUtils.equals(objectState.getString("par"), "single")) {

                Element messageElement = rootElement.addElement("Message");
                messageElement.addElement("MessageID").setText(messageId + "");
                messageElement.addElement("OperationType").setText("Update");
                Element relationshipElement = messageElement.addElement("Relationship");

                for (int i = 0; i < array.size(); i++) {
                    JSONObject object = JSONObject.parseObject(array.get(i).toString());
                    if (StringUtils.equals(object.getString("par"), "parent")) {
                        relationshipElement.addElement("ParentSKU").setText(object.getString("sku"));
                        break;
                    }
                }

                for (int j = 0; j < array.size(); j++) {
                    JSONObject object = JSONObject.parseObject(array.get(j).toString());
                    if (StringUtils.equals(object.getString("par"), "child")) {
                        Element relationElement = relationshipElement.addElement("Relation");
                        relationElement.addElement("SKU").setText(object.getString("sku"));
                        relationElement.addElement("Type").setText("Variation");


                    }

                }

                messageId++;
            }

        }

        //如果messageId为1时标识当前批次商品全部为没有变体商品不需要生成关联关系信息
        if (messageId == 1) {
            return "";
        }

//TODO
        createFilePath();


        Date dd = new Date();
        String name = dd.getTime() + "guanlian";
        //TODO
        return createXmlFile(document, name);

    }

    /**
     * 创建亚马逊商品图片xml
     *
     * @throws DocumentException
     * @throws IOException
     */
    public String createImageXml(JSONArray arrays) throws DocumentException, IOException {
//		File file1 = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "amazon\\image.xml");
        InputStream inputStream = this.getClass().getResourceAsStream("/amazon/image.xml");


        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(inputStream);
        Element rootElement = document.getRootElement();


        int messageId = 1;//定义xml文件中的messageid值(每个xml文件中messageid不重复)
        for (int f = 0; f < arrays.size(); f++) {
            JSONObject objct = JSONObject.parseObject(arrays.get(f).toString());
            JSONArray array = objct.getJSONArray("data");

            for (int i = 0; i < array.size(); i++) {

                JSONObject object = JSONObject.parseObject(array.getString(i).toString());
                if (StringUtils.equals(object.getString("par"), "child")) {//当商品信息为变体时验证通过
                    JSONArray imageArray = JSONObject.parseArray(object.getString("images"));//获取变体商品图片信息
                    //如果图片的长度超过9张
                    JSONArray imageArrays = new JSONArray();
                    if (imageArray.size() > 9) {
                        for (int p = 0; p < 9; p++) {
                            imageArrays.add(imageArray.get(p));
                        }
                    } else {
                        imageArrays = imageArray;
                    }

                    for (int j = 0; j < imageArrays.size(); j++) {

                        Element messageElement = rootElement.addElement("Message");
                        messageElement.addElement("MessageID").setText(messageId + "");
                        messageElement.addElement("OperationType").setText("Update");
                        Element relationshipElement = messageElement.addElement("ProductImage");
                        relationshipElement.addElement("SKU").setText(object.getString("sku"));
                        if (j == 0) {
                            relationshipElement.addElement("ImageType").setText("Main");//设置主图片
                        } else {
                            relationshipElement.addElement("ImageType").setText("PT" + j);//设置PT标识图片
                        }

                        relationshipElement.addElement("ImageLocation").setText(imageArrays.get(j).toString());

                        messageId++;
                    }
                    //当不上传变体时
                } else if (StringUtils.equals(object.getString("par"), "single")) {
                    JSONArray imageArray = JSONObject.parseArray(object.getString("images"));//获取变体商品图片信息
                    //如果图片的长度超过9张
                    JSONArray imageArrays = new JSONArray();
                    if (imageArray.size() > 9) {
                        for (int p = 0; p < 9; p++) {
                            imageArrays.add(imageArray.get(p));
                        }
                    } else {
                        imageArrays = imageArray;
                    }
                    for (int j = 0; j < imageArrays.size(); j++) {

                        Element messageElement = rootElement.addElement("Message");
                        messageElement.addElement("MessageID").setText(messageId + "");
                        messageElement.addElement("OperationType").setText("Update");
                        Element relationshipElement = messageElement.addElement("ProductImage");
                        relationshipElement.addElement("SKU").setText(object.getString("sku"));
                        if (j == 0) {
                            relationshipElement.addElement("ImageType").setText("Main");//设置主图片
                        } else {
                            relationshipElement.addElement("ImageType").setText("PT" + j);//设置PT标识图片
                        }

                        relationshipElement.addElement("ImageLocation").setText(imageArrays.get(j).toString());

                        messageId++;
                    }
                }
            }

        }

//TODO
        createFilePath();
        Date dd = new Date();
        String name = dd.getTime() + "tupian";
        //TODO
        return createXmlFile(document, name);

    }

    /**
     * 创建亚马逊商品价格xml
     *
     * @throws DocumentException
     * @throws IOException
     */
    public String createPriceXml(JSONArray arrays, String marktPlaceId) throws DocumentException, IOException {

//		File file1 = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "amazon\\price.xml");
        InputStream inputStream = this.getClass().getResourceAsStream("/amazon/price.xml");


        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(inputStream);
        Element rootElement = document.getRootElement();


        int messageId = 1;//定义xml文件中的messageid值(每个xml文件中messageid不重复)
        for (int f = 0; f < arrays.size(); f++) {
            JSONObject objct = JSONObject.parseObject(arrays.get(f).toString());
            JSONArray array = objct.getJSONArray("data");

            for (int i = 0; i < array.size(); i++) {

                JSONObject object = JSONObject.parseObject(array.getString(i).toString());

                if (StringUtils.equals(object.getString("par"), "child")) {
                    Element messageElement = rootElement.addElement("Message");
                    messageElement.addElement("MessageID").setText(messageId + "");
                    messageElement.addElement("OperationType").setText("Update");
                    Element priceElement = messageElement.addElement("Price");
                    priceElement.addElement("SKU").setText(object.getString("sku"));

                    //判断区域对应的货币
                    if (StringUtils.equals("US", marktPlaceId)) {
                        priceElement.addElement("StandardPrice").addAttribute("currency", "USD").setText(object.getString("price"));
                    } else if (StringUtils.equals("CA", marktPlaceId)) {
                        priceElement.addElement("StandardPrice").addAttribute("currency", "CAD").setText(object.getString("price"));
                    } else if (StringUtils.equals("JP", marktPlaceId)) {
                        priceElement.addElement("StandardPrice").addAttribute("currency", "JPY").setText(object.getString("price"));
                    } else if (StringUtils.equals("GB", marktPlaceId)) {
                        priceElement.addElement("StandardPrice").addAttribute("currency", "GBP").setText(object.getString("price"));
                    } else if (StringUtils.equals("DE", marktPlaceId)) {
                        priceElement.addElement("StandardPrice").addAttribute("currency", "EUR").setText(object.getString("price"));
                    } else if (StringUtils.equals("IN", marktPlaceId)) {
                        priceElement.addElement("StandardPrice").addAttribute("currency", "INR").setText(object.getString("price"));
                    } else {
                        priceElement.addElement("StandardPrice").addAttribute("currency", "DEFAULT").setText(object.getString("price"));
                    }

                    Element saleElement = priceElement.addElement("Sale");
                    saleElement.addElement("StartDate").setText("2019-08-26T00:00:00Z");
                    saleElement.addElement("EndDate").setText("2025-09-26T00:00:00Z");

                    //判断区域对应的货币
                    if (StringUtils.equals("US", marktPlaceId)) {
                        saleElement.addElement("SalePrice").addAttribute("currency", "USD").setText(object.getString("price"));
                    } else if (StringUtils.equals("CA", marktPlaceId)) {
                        saleElement.addElement("SalePrice").addAttribute("currency", "CAD").setText(object.getString("price"));
                    } else if (StringUtils.equals("JP", marktPlaceId)) {
                        saleElement.addElement("SalePrice").addAttribute("currency", "JPY").setText(object.getString("price"));
                    } else if (StringUtils.equals("GB", marktPlaceId)) {
                        saleElement.addElement("SalePrice").addAttribute("currency", "GBP").setText(object.getString("price"));
                    } else if (StringUtils.equals("DE", marktPlaceId)) {
                        saleElement.addElement("SalePrice").addAttribute("currency", "EUR").setText(object.getString("price"));
                    } else if (StringUtils.equals("IN", marktPlaceId)) {
                        saleElement.addElement("SalePrice").addAttribute("currency", "INR").setText(object.getString("price"));
                    } else {
                        saleElement.addElement("SalePrice").addAttribute("currency", "DEFAULT").setText(object.getString("price"));
                    }


                } else if (StringUtils.equals(object.getString("par"), "single")) {
                    Element messageElement = rootElement.addElement("Message");
                    messageElement.addElement("MessageID").setText(messageId + "");
                    messageElement.addElement("OperationType").setText("Update");
                    Element priceElement = messageElement.addElement("Price");
                    priceElement.addElement("SKU").setText(object.getString("sku"));

                    //判断区域对应的货币
                    if (StringUtils.equals("US", marktPlaceId)) {
                        priceElement.addElement("StandardPrice").addAttribute("currency", "USD").setText(object.getString("price"));
                    } else if (StringUtils.equals("CA", marktPlaceId)) {
                        priceElement.addElement("StandardPrice").addAttribute("currency", "CAD").setText(object.getString("price"));
                    } else if (StringUtils.equals("JP", marktPlaceId)) {
                        priceElement.addElement("StandardPrice").addAttribute("currency", "JPY").setText(object.getString("price"));
                    } else if (StringUtils.equals("GB", marktPlaceId)) {
                        priceElement.addElement("StandardPrice").addAttribute("currency", "GBP").setText(object.getString("price"));
                    } else if (StringUtils.equals("DE", marktPlaceId)) {
                        priceElement.addElement("StandardPrice").addAttribute("currency", "EUR").setText(object.getString("price"));
                    } else if (StringUtils.equals("IN", marktPlaceId)) {
                        priceElement.addElement("StandardPrice").addAttribute("currency", "INR").setText(object.getString("price"));
                    } else {
                        priceElement.addElement("StandardPrice").addAttribute("currency", "DEFAULT").setText(object.getString("price"));

                    }

                    Element saleElement = priceElement.addElement("Sale");
                    saleElement.addElement("StartDate").setText("2019-08-26T00:00:00Z");
                    saleElement.addElement("EndDate").setText("2025-09-26T00:00:00Z");

                    //判断区域对应的货币
                    if (StringUtils.equals("US", marktPlaceId)) {
                        saleElement.addElement("SalePrice").addAttribute("currency", "USD").setText(object.getString("price"));
                    } else if (StringUtils.equals("CA", marktPlaceId)) {
                        saleElement.addElement("SalePrice").addAttribute("currency", "CAD").setText(object.getString("price"));
                    } else if (StringUtils.equals("JP", marktPlaceId)) {
                        saleElement.addElement("SalePrice").addAttribute("currency", "JPY").setText(object.getString("price"));
                    } else if (StringUtils.equals("GB", marktPlaceId)) {
                        saleElement.addElement("SalePrice").addAttribute("currency", "GBP").setText(object.getString("price"));
                    } else if (StringUtils.equals("DE", marktPlaceId)) {
                        saleElement.addElement("SalePrice").addAttribute("currency", "EUR").setText(object.getString("price"));
                    } else if (StringUtils.equals("IN", marktPlaceId)) {
                        saleElement.addElement("SalePrice").addAttribute("currency", "INR").setText(object.getString("price"));
                    } else {
                        saleElement.addElement("SalePrice").addAttribute("currency", "DEFAULT").setText(object.getString("price"));
                    }


                }
                messageId++;

            }
        }

//TODO
        createFilePath();

        Date dd = new Date();
        String name = dd.getTime() + "jiage";
        //TODO
        return createXmlFile(document, name);

    }

    /**
     * 创建亚马逊商品库存xml
     *
     * @throws DocumentException
     * @throws IOException
     */
    public String createQuantityXml(JSONArray arrays) throws DocumentException, IOException {

//		File file1 = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "amazon\\quantity.xml");
        InputStream inputStream = this.getClass().getResourceAsStream("/amazon/quantity.xml");

        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(inputStream);
        Element rootElement = document.getRootElement();

        int messageId = 1;//定义xml文件中的messageid值(每个xml文件中messageid不重复)
        for (int f = 0; f < arrays.size(); f++) {
            JSONObject objct = JSONObject.parseObject(arrays.get(f).toString());
            JSONArray array = objct.getJSONArray("data");

            for (int i = 0; i < array.size(); i++) {
                JSONObject object = JSONObject.parseObject(array.getString(i).toString());
                if (StringUtils.equals(object.getString("par"), "child")) {
                    Element messageElement = rootElement.addElement("Message");
                    messageElement.addElement("MessageID").setText(messageId + "");
                    messageElement.addElement("OperationType").setText("Update");
                    Element inventoryElement = messageElement.addElement("Inventory");
                    inventoryElement.addElement("SKU").setText(object.getString("sku"));
                    inventoryElement.addElement("FulfillmentCenterID").setText("DEFAULT");
                    inventoryElement.addElement("Quantity").setText(object.getString("quantity"));
                    inventoryElement.addElement("FulfillmentLatency").setText("1");
                    inventoryElement.addElement("SwitchFulfillmentTo").setText("MFN");
                    messageId++;
                } else if (StringUtils.equals(object.getString("par"), "single")) {
                    Element messageElement = rootElement.addElement("Message");
                    messageElement.addElement("MessageID").setText(messageId + "");
                    messageElement.addElement("OperationType").setText("Update");
                    Element inventoryElement = messageElement.addElement("Inventory");
                    inventoryElement.addElement("SKU").setText(object.getString("sku"));
                    inventoryElement.addElement("FulfillmentCenterID").setText("DEFAULT");
                    inventoryElement.addElement("Quantity").setText(object.getString("quantity"));
                    inventoryElement.addElement("FulfillmentLatency").setText("1");
                    inventoryElement.addElement("SwitchFulfillmentTo").setText("MFN");
                    messageId++;
                }
            }
        }
//TODO
        createFilePath();
        Date dd = new Date();
        String name = dd.getTime() + "kucun";
        //TODO
        return createXmlFile(document, name);

    }

    public void createFilePath() {
        if (MallConstant.LINUX_ENVIRONMENT) {
            //linux判断该文件夹是否存在
            File file = new File("/data/amazon");
            if (!file.exists()) {
                file.mkdirs();
            }
        } else {
            //windows判断该文件夹是否存在
            File file = new File("C:\\amazon");
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }

    public String createXmlFile(Document document, String name) throws IOException {
        String allName = "";
        if (MallConstant.LINUX_ENVIRONMENT) {
            //linux文件全路径
            allName = "/data/amazon/" + name + ".xml";
        } else {
            //windows文件全路径
            allName = "C:\\amazon\\" + name + ".xml";
        }
        FileOutputStream fileOutputStream = new FileOutputStream(allName);
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        format.setNewLineAfterDeclaration(false);
        XMLWriter xMLWriter = new XMLWriter(fileOutputStream, format);
        xMLWriter.write(document);
        try {
            xMLWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allName;
    }
}
