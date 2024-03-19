package cn.bravedawn.rabbit.task.annotation;

import java.lang.annotation.*;

/**
 * @author : depers
 * @program : rabbit-parent
 * @description: elastic job配置
 * @date : Created in 2021/3/14 17:54
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface ElasticJobConfig {

    String name();    //elasticjob的名称

    String cron() default "";

    int shardingTotalCount() default 1;

    String shardingItemParameters() default "";

    String jobParameter() default "";

    boolean failover() default false;

    boolean misfire() default true;

    String description() default "";

    boolean overwrite() default false;

    boolean streamingProcess() default false;

    String scriptCommandLine() default "";

    boolean monitorExecution() default false;

    int monitorPort() default -1;    //must

    int maxTimeDiffSeconds() default -1;    //must

    String jobShardingStrategyClass() default "";    //must

    int reconcileIntervalMinutes() default 10;    //must

    String eventTraceRdbDataSource() default "";    //must

    String listener() default "";    //must

    boolean disabled() default false;    //must

    String distributedListener() default "";

    long startedTimeoutMilliseconds() default Long.MAX_VALUE;    //must

    long completedTimeoutMilliseconds() default Long.MAX_VALUE;        //must

    String jobExceptionHandler() default "com.dangdang.ddframe.job.executor.handler.impl.DefaultJobExceptionHandler";

    String executorServiceHandler() default "com.dangdang.ddframe.job.executor.handler.impl.DefaultExecutorServiceHandler";


}
