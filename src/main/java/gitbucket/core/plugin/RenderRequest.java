/*******************************************************************************
 * Copyright (c) 2018-11-09 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package gitbucket.core.plugin;

import gitbucket.core.controller.Context;
import gitbucket.core.service.RepositoryService;

import java.util.List;

/**
 * RenderRequest
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-09
 * auto generate by qdp.
 */
public class RenderRequest {
    List<String> filePath;
    String fileContent;
    String branch;
    RepositoryService.RepositoryInfo repository;
    boolean enableWikiLink;
    boolean enableRefsLink;
    boolean enableAnchor;
    Context context;

    public RenderRequest(List<String> filePath, String fileContent, String branch, RepositoryService.RepositoryInfo repository, boolean enableWikiLink, boolean enableRefsLink, boolean enableAnchor, Context context) {
        this.filePath = filePath;
        this.fileContent = fileContent;
        this.branch = branch;
        this.repository = repository;
        this.enableWikiLink = enableWikiLink;
        this.enableRefsLink = enableRefsLink;
        this.enableAnchor = enableAnchor;
        this.context = context;
    }

    public List<String> filePath() {
        return filePath;
    }

    public String fileContent() {
        return fileContent;
    }

    public String branch() {
        return branch;
    }

    public RepositoryService.RepositoryInfo repository() {
        return repository;
    }

    public boolean enableWikiLink() {
        return enableWikiLink;
    }

    public boolean enableRefsLink() {
        return enableRefsLink;
    }

    public boolean enableAnchor() {
        return enableAnchor;
    }

    public Context context() {
        return context;
    }

    public List<String> getFilePath() {
        return filePath;
    }

    public void setFilePath(List<String> filePath) {
        this.filePath = filePath;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public RepositoryService.RepositoryInfo getRepository() {
        return repository;
    }

    public void setRepository(RepositoryService.RepositoryInfo repository) {
        this.repository = repository;
    }

    public boolean isEnableWikiLink() {
        return enableWikiLink;
    }

    public void setEnableWikiLink(boolean enableWikiLink) {
        this.enableWikiLink = enableWikiLink;
    }

    public boolean isEnableRefsLink() {
        return enableRefsLink;
    }

    public void setEnableRefsLink(boolean enableRefsLink) {
        this.enableRefsLink = enableRefsLink;
    }

    public boolean isEnableAnchor() {
        return enableAnchor;
    }

    public void setEnableAnchor(boolean enableAnchor) {
        this.enableAnchor = enableAnchor;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
