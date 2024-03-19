package cn.bravedawn.rabbit.task.enums;

/**
 * @author : depers
 * @program : rabbit-parent
 * @description: Job类型枚举定义
 * @date : Created in 2021/3/14 21:43
 */
public enum ElasticJobTypeEnum {


    SIMPLE("SimpleJob", "简单Job"),
    DATAFLOW("DataflowJob", "流式Job"),
    SCRIPT("ScriptJob", "脚本类型Job");

    private String type;

    private String desc;

    ElasticJobTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
