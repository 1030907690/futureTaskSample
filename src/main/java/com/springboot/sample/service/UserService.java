package com.springboot.sample.service;

import com.springboot.sample.future.CustomCallable;
import com.springboot.sample.future.CustomFutureTask;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class UserService {
    // CPU核数
    private final int processors = Runtime.getRuntime().availableProcessors();
    private final ExecutorService executorService = new ThreadPoolExecutor(processors * 2, processors * 10, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue(processors * 100), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());

    /***
     * 查询用户条数
     * */
    private int userCount() {
        try {
            //模拟这里查询数据库耗时2毫秒
            Thread.sleep(2L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 10;
    }

    /***
     * 根据页数查询用户具体数据
     * @param currentPage 页数
     * */
    private List userData(int currentPage) {
        try {
            //模拟这里查询数据库耗时2毫秒
            Thread.sleep(2L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    public Object findUserPageList1() {
        long startTime = System.currentTimeMillis();
        //1、查询用户条数
        int userCount = userCount();
        System.out.println(userCount);
        //2、分页查询用户数据
        List list = userData(1);
        System.out.println(list);
        long endTime = System.currentTimeMillis();
        // 相差时间，耗时
        long diffTime = endTime - startTime;
        System.out.println("总共耗时：" + diffTime+"毫秒");
        return null;
    }

    public Object findUserPageList2() {
        long startTime = System.currentTimeMillis();
        //1、查询用户条数
        // userCount();
        Callable userCountFutureCallable = new Callable() {
            @Override
            public Integer call() throws Exception {
                return userCount();
            }
        };
        FutureTask<Integer> userCountFuture = new FutureTask(userCountFutureCallable);
        //2、分页查询用户数据
        // userData(1);
        Callable userDataFutureCallable = new Callable() {
            @Override
            public List call() throws Exception {
                return userData(1);
            }
        };
        FutureTask<List> userDataFuture = new FutureTask(userDataFutureCallable);
        // 使用子线程执行
        /*new Thread(userCountFuture).start();
        new Thread(userDataFuture).start();
*/
        executorService.execute(userCountFuture);
        executorService.execute(userDataFuture);

        Integer userCount = 0;
        try {
            userCount = userCountFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(userCount);
        List list = null;
        try {
            list = userDataFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(list);
        long endTime = System.currentTimeMillis();
        // 相差时间，耗时
        long diffTime = endTime - startTime;
        System.out.println("总共耗时：" + diffTime+"毫秒");
        return null;
    }

    public Object findUserPageList() {
        long startTime = System.currentTimeMillis();
        //1、查询用户条数
        // userCount();
        CustomCallable userCountFutureCallable = new CustomCallable() {
            @Override
            public Integer call() throws Exception {
                return userCount();
            }
        };
        CustomFutureTask<Integer> userCountFuture = new CustomFutureTask(userCountFutureCallable);
        //2、分页查询用户数据
        // userData(1);
        CustomCallable userDataFutureCallable = new CustomCallable() {
            @Override
            public List call() throws Exception {
                return userData(1);
            }
        };
        CustomFutureTask<List> userDataFuture = new CustomFutureTask(userDataFutureCallable);
        // 使用子线程执行
        new Thread(userCountFuture).start();
        new Thread(userDataFuture).start();

        Integer userCount = 0;
        try {
            userCount = userCountFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(userCount);
        List list = null;
        try {
            list = userDataFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(list);
        long endTime = System.currentTimeMillis();
        // 相差时间，耗时
        long diffTime = endTime - startTime;
        System.out.println("总共耗时：" + diffTime+"毫秒");
        return null;
    }

}
