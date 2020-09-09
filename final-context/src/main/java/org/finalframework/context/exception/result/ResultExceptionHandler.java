

package org.finalframework.context.exception.result;

import org.finalframework.annotation.result.Result;
import org.finalframework.core.exception.ExceptionHandler;

/**
 * {@link Result}异常处理器，将异常所携带的异常消息封装成 {@link Result}对象返回给调用方。
 *
 * @author likly
 * @version 1.0
 * @date 2018-10-31 13:14
 * @see IExceptionResultExceptionHandler
 * @see ViolationResultExceptionHandler
 * @see UnCatchResultExceptionHandler
 * @since 1.0
 */
public interface ResultExceptionHandler<E> extends ExceptionHandler<E, Result<?>> {

}
