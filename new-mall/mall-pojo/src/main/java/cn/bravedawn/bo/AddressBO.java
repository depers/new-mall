package cn.bravedawn.bo;

import lombok.Data;

/**
 * @Author : fengx9
 * @Project : new-mall
 * @Date : Created in 2024-01-04 16:53
 *
 * 用户新增或修改地址的BO
 */

@Data
public class AddressBO {

    private String addressId;
    private String userId;
    private String receiver;
    private String mobile;
    private String province;
    private String city;
    private String district;
    private String detail;
}
