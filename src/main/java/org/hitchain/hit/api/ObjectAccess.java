/*******************************************************************************
 * Copyright (c) 2018-11-21 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.hitchain.hit.api;

import java.io.InputStream;

/**
 * ObjectAccess
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-21
 * auto generate by qdp.
 */
public interface ObjectAccess {
    InputStream get(String objectHash);

    InputStream ls(String objectHash);

    InputStream post(HashedFile files);
}
