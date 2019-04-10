package org.hippo.jwts.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author dujf
 * @since 2018-12-29
 */
public class SysPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 是否可用[0否1是]
     */
    private Integer available;

    /**
     * 权限名
     */
    private String name;

    /**
     * 父节点
     */
    private Integer parentId;

    /**
     * 所有父节点
     */
    private String parentIds;

    /**
     * 权限
     */
    private String permission;

    /**
     * 权限类型
     */
    private String resourceType;

    /**
     * 权限链接
     */
    private String url;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "SysPermission{" +
        "id=" + id +
        ", available=" + available +
        ", name=" + name +
        ", parentId=" + parentId +
        ", parentIds=" + parentIds +
        ", permission=" + permission +
        ", resourceType=" + resourceType +
        ", url=" + url +
        "}";
    }
}
