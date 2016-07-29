package zy.chasegoddness.global;

import android.os.Bundle;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * 使用效果同EventBus 用Rxjava实现
 * 用于同一进程下的不同组件传递消息事件
 * 比如Activity和Service之间的交互 可以通过RxBus来传递而不用IBinder或IPC等复杂的通讯
 * <br>
 * 参考资料：http://nerds.weddingpartyapp.com/tech/2014/12/24/implementing-an-event-bus-with-rxjava-rxbus/
 */
public class RxBus {
    /**
     * 事件传输总线 使用Subject既可充当Observable也可充当Observer 相当于一个衔接两头的通道<br>
     * PublishSubject会向观察者发射自它开始订阅以后的事件<br>
     * 不同于ReplySubject会向观察者发射所有事件 包括在订阅之前的事件<br>
     * 也不同于BehaviourSubject除以后的事件外还会向观察者发射最近的一个事件<br>
     * 用SerializedSubject装饰可以使发射的事件串行化，防止多线程同时发射事件<br>
     */
    private final Subject<RxEvent, RxEvent> _bus = new SerializedSubject<>(PublishSubject.create());
    private static final RxBus INSTANCE = new RxBus();

    private RxBus() {
    }

    /**
     * 用以下方法发送一个事件
     */
    //    _rxBus.send(new TapEvent());

    /**
     * 用以下方法订阅事件
     */
    //    _rxBus.toObserverable().subscribe(new Action1<Object>() {
    //        public void call (Object event){
    //            if (event instanceof TapEvent)
    //                doSomething;
    //            else if (event instanceof SomeOtherEvent)
    //                doSomethingElse();
    //        }
    //    });
    public static RxBus getInstance() {
        return INSTANCE;
    }

    public void send(RxEvent o) {
        _bus.onNext(o);
    }

    public Observable<RxEvent> toObserverable() {
        return _bus;
    }

    public static class RxEvent {
        private int id;
        private String desc;

        public int getId() {
            return id;
        }

        public RxEvent id(int id) {
            this.id = id;
            return this;
        }

        public String getDesc() {
            return desc;
        }

        public RxEvent desc(String desc) {
            this.desc = desc;
            return this;
        }
    }
}
