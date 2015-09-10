package oracle.webcenter.sites.framework.analytics.taglibs;

import javax.servlet.jsp.*;

import oracle.webcenter.sites.framework.analytics.model.Asset;

import org.apache.commons.beanutils.BeanUtils;

public class GetAssetAttributeValueTag extends AbstractTagSupport {

    public GetAssetAttributeValueTag() {
        asset = null;
        property = null;
    }

    public int doStartTag() throws JspException {
        begin();
        try {
            if (logger.isDebugEnabled()) {
                logger.debug((new StringBuilder()).append("Asset           : ").append(asset).toString());
                logger.debug((new StringBuilder()).append("Asset populated : ").append(asset.populated()).toString());
                logger.debug((new StringBuilder()).append("Property        : ").append(getProperty()).toString());
            }
            if (!asset.populated()) {
                pageContext.include((new StringBuilder()).append("/reports/analytics/jsp/asset_detail.jsp?assetType=").append(asset.getType()).append("&assetId=").append(asset.getId()).toString());
                String assetLabel = (String) pageContext.getAttribute("currentAssetName", 2);
                if (logger.isDebugEnabled())
                    logger.debug((new StringBuilder()).append("Asset Label     : ").append(assetLabel).toString());
                asset.setLabel(assetLabel);
                setC(asset.getType());
                setCid(asset.getId());
                setLabel(assetLabel);
                updateAssetDetail();
            }
            String value = BeanUtils.getProperty(getAsset(), getProperty());
            pageContext.getOut().write(value);
        } catch (Exception expGeneral) {
            throw new JspException(expGeneral);
        }
        end();
        return 0;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }

    private static final long serialVersionUID = 1L;
    private Asset asset;
    private String property;
}
