/*******************************************************************************
 * Copyright (c) 2018-11-20 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.hitchain.hit.api;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * SmartGit
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-20
 * auto generate by qdp.
 */
public interface SmartGit {

    InputStream getInfoRefs();

    InputStream getHead();

    InputStream getObjects(String objectHash);

    InputStream getObjectsInfoHttpAlternates();

    InputStream getObjectsInfoPacks();

    InputStream getObjectsPackIdx(String packHash);

    InputStream getObjectsPackPack(String packHash);

    InputStream getGitReceivePack();

    InputStream postGitReceivePack(OutputStream os);

    InputStream getGitUploadPack();

    InputStream postGitUploadPack(OutputStream os);
}
