/*******************************************************************************
 * Copyright (c) 2018-11-08 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package gitbucket.core.util;

import org.apache.commons.lang3.StringUtils;

/**
 * RepositoryName
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-08
 * auto generate by qdp.
 */
// TODO Move to gitbucket.core.api package?
public class RepositoryName {
    public String fullName;
    private String owner;
    private String name;

    public RepositoryName(String owner, String name) {
        this.owner = owner;
        this.name = name;
        this.fullName = owner + "/" + name;
    }

    public RepositoryName(String fullName) {
        String[] split = StringUtils.split(fullName, '/');
        if (!(split.length > 1 && StringUtils.isNotBlank(split[0]) && StringUtils.isNotBlank(split[1]))) {
            throw new IllegalArgumentException(fullName + " is not repositoryName (only 'owner/name')");
        }
        this.owner = split[0];
        this.name = split[1];
        this.fullName = owner + "/" + name;
    }

    public RepositoryName(gitbucket.core.model.Repository repository) {
        this(repository.userName(), repository.repositoryName());
    }

    public RepositoryName(gitbucket.core.util.JGitUtil.RepositoryInfo repository) {
        this(repository.owner(), repository.name());
    }

    public RepositoryName(gitbucket.core.service.RepositoryService.RepositoryInfo repository) {
        this(repository.owner(), repository.name());
    }

    public RepositoryName(gitbucket.core.model.CommitStatus repository) {
        this(repository.userName(), repository.repositoryName());
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
