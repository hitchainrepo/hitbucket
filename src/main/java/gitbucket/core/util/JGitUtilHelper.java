/*******************************************************************************
 * Copyright (c) 2018-11-02 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package gitbucket.core.util;

import java.io.*;

import gitbucket.core.service.RepositoryService;
import org.cache2k.Cache;
import org.cache2k.CacheEntry;
import org.eclipse.jgit.api.Git;
import static gitbucket.core.util.Directory.*;
import static gitbucket.core.util.StringUtil.*;
import static gitbucket.core.util.SyntaxSugars.*;

import org.slf4j.Logger;
import scala.Function0;
import scala.Function1;
import scala.annotation.tailrec;
import static scala.collection.JavaConverters.*;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.*;
import org.eclipse.jgit.revwalk.filter.*;
import org.eclipse.jgit.treewalk.*;
import org.eclipse.jgit.treewalk.filter.*;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.errors.*;
import org.eclipse.jgit.transport.RefSpec;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.cache2k.Cache2kBuilder;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.diff.*;
import org.eclipse.jgit.dircache.DirCacheEntry;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.slf4j.LoggerFactory;

/**
 * Provides complex JGit operations.
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-02
 * auto generate by qdp.
 */
public class JGitUtilHelper {
    private static final Logger logger = LoggerFactory.getLogger(JGitUtilHelper.class);


  public static class RepositoryInfo {
        protected  String owner;
        protected String name;
        protected List<String> branchList;
        protected List<TagInfo> tags;

        public RepositoryInfo() {
        }


       /**
       * The repository data.
       *
       * @param owner the user name of the repository owner
       * @param name the repository name
       * @param branchList the list of branch names
       * @param tags the list of tags
       */
        public RepositoryInfo(String owner, String name, List<String> branchList, List<TagInfo> tags) {
            this.owner = owner;
            this.name = name;
            this.branchList = branchList;
            this.tags = tags;
        }

        public RepositoryInfo(String owner, String name) {
            this(owner,name,null,null);
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

        public List<String> getBranchList() {
            return branchList;
        }

        public void setBranchList(List<String> branchList) {
            this.branchList = branchList;
        }

        public List<TagInfo> getTags() {
            return tags;
        }

        public void setTags(List<TagInfo> tags) {
            this.tags = tags;
        }
    }


  public static class FileInfo{
         protected  ObjectId id;
        protected boolean isDirectory;
        protected String name;
        protected String  path;
        protected String message;
        protected String  commitId;
        protected Date time;
        protected String  author;
        protected String  mailAddress;
        protected String  linkUrl;

      public FileInfo() {
      }
      /**
       * The file data for the file list of the repository viewer.
       *
       * @param id the object id
       * @param isDirectory whether is it directory
       * @param name the file (or directory) name
       * @param path the file (or directory) complete path
       * @param message the last commit message
       * @param commitId the last commit id
       * @param time the last modified time
       * @param author the last committer name
       * @param mailAddress the committer's mail address
       * @param linkUrl the url of submodule
       */
      public FileInfo(ObjectId id, boolean isDirectory, String name, String path, String message, String commitId, Date time, String author, String mailAddress, String linkUrl) {
          this.id = id;
          this.isDirectory = isDirectory;
          this.name = name;
          this.path = path;
          this.message = message;
          this.commitId = commitId;
          this.time = time;
          this.author = author;
          this.mailAddress = mailAddress;
          this.linkUrl = linkUrl;
      }

      public ObjectId getId() {
            return id;
        }

        public void setId(ObjectId id) {
            this.id = id;
        }

        public boolean isDirectory() {
            return isDirectory;
        }

        public void setDirectory(boolean directory) {
            isDirectory = directory;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCommitId() {
            return commitId;
        }

        public void setCommitId(String commitId) {
            this.commitId = commitId;
        }

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getMailAddress() {
            return mailAddress;
        }

        public void setMailAddress(String mailAddress) {
            this.mailAddress = mailAddress;
        }

        public String getLinkUrl() {
            return linkUrl;
        }

        public void setLinkUrl(String linkUrl) {
            this.linkUrl = linkUrl;
        }
    }


        public static class CommitInfo{

            protected String id;
            protected String shortMessage;
            protected String fullMessage;
            protected List<String> parents;
            protected Date authorTime;
            protected String authorName;
            protected String authorEmailAddress;
            protected Date commitTime;
            protected String committerName;
            protected String committerEmailAddress;

            public CommitInfo() {
            }

            /**
             * The commit data.
             *
             * @param id the commit id
             * @param shortMessage the short message
             * @param fullMessage the full message
             * @param parents the list of parent commit id
             * @param authorTime the author time
             * @param authorName the author name
             * @param authorEmailAddress the mail address of the author
             * @param commitTime the commit time
             * @param committerName  the committer name
             * @param committerEmailAddress the mail address of the committer
             */
            public CommitInfo(String id, String shortMessage, String fullMessage, List<String> parents, Date authorTime, String authorName, String authorEmailAddress, Date commitTime, String committerName, String committerEmailAddress) {
                this.id = id;
                this.shortMessage = shortMessage;
                this.fullMessage = fullMessage;
                this.parents = parents;
                this.authorTime = authorTime;
                this.authorName = authorName;
                this.authorEmailAddress = authorEmailAddress;
                this.commitTime = commitTime;
                this.committerName = committerName;
                this.committerEmailAddress = committerEmailAddress;
            }

            public CommitInfo(org.eclipse.jgit.revwalk.RevCommit rev) {
                this(
                    rev.getName(),
                    rev.getShortMessage(),
                    rev.getFullMessage(),
                    parentNames(rev.getParents()),
                    rev.getAuthorIdent().getWhen(),
                    rev.getAuthorIdent().getName(),
                    rev.getAuthorIdent().getEmailAddress(),
                    rev.getCommitterIdent().getWhen(),
                    rev.getCommitterIdent().getName(),
                    rev.getCommitterIdent().getEmailAddress()
                );
            }
        public void summary(){
            //TODO return getSummaryMessage(fullMessage, shortMessage);
        }

        public String description(){
            int i = fullMessage.trim().indexOf('\n');
            if(i>=0){
                return fullMessage.trim().substring(i).trim();
            }else{
                return null;
            }
        }

        public boolean isDifferentFromAuthor(){
                return authorName != committerName || authorEmailAddress != committerEmailAddress;
        }
        public static List<String> parentNames(RevCommit[] revs){
                List<String> list = new ArrayList<>();
                for(RevCommit rev:revs){
                    list.add(rev.getName());
                }
                return list;
        }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getShortMessage() {
                return shortMessage;
            }

            public void setShortMessage(String shortMessage) {
                this.shortMessage = shortMessage;
            }

            public String getFullMessage() {
                return fullMessage;
            }

            public void setFullMessage(String fullMessage) {
                this.fullMessage = fullMessage;
            }

            public List<String> getParents() {
                return parents;
            }

            public void setParents(List<String> parents) {
                this.parents = parents;
            }

            public Date getAuthorTime() {
                return authorTime;
            }

            public void setAuthorTime(Date authorTime) {
                this.authorTime = authorTime;
            }

            public String getAuthorName() {
                return authorName;
            }

            public void setAuthorName(String authorName) {
                this.authorName = authorName;
            }

            public String getAuthorEmailAddress() {
                return authorEmailAddress;
            }

            public void setAuthorEmailAddress(String authorEmailAddress) {
                this.authorEmailAddress = authorEmailAddress;
            }

            public Date getCommitTime() {
                return commitTime;
            }

            public void setCommitTime(Date commitTime) {
                this.commitTime = commitTime;
            }

            public String getCommitterName() {
                return committerName;
            }

            public void setCommitterName(String committerName) {
                this.committerName = committerName;
            }

            public String getCommitterEmailAddress() {
                return committerEmailAddress;
            }

            public void setCommitterEmailAddress(String committerEmailAddress) {
                this.committerEmailAddress = committerEmailAddress;
            }
        }

  public static class DiffInfo{
    protected  ChangeType changeType;
    protected  String oldPath;
      protected  String newPath;
      protected  String oldContent;
      protected  String newContent;
      protected  boolean oldIsImage;
      protected  boolean newIsImage;
      protected  String oldObjectId;
      protected  String newObjectId;
      protected  String oldMode;
      protected  String newMode;
      protected  boolean tooLarge;
      protected  String patch;

      public DiffInfo() {
      }

      public ChangeType getChangeType() {
          return changeType;
      }

      public void setChangeType(ChangeType changeType) {
          this.changeType = changeType;
      }

      public String getOldPath() {
          return oldPath;
      }

      public void setOldPath(String oldPath) {
          this.oldPath = oldPath;
      }

      public String getNewPath() {
          return newPath;
      }

      public void setNewPath(String newPath) {
          this.newPath = newPath;
      }

      public String getOldContent() {
          return oldContent;
      }

      public void setOldContent(String oldContent) {
          this.oldContent = oldContent;
      }

      public String getNewContent() {
          return newContent;
      }

      public void setNewContent(String newContent) {
          this.newContent = newContent;
      }

      public boolean isOldIsImage() {
          return oldIsImage;
      }

      public void setOldIsImage(boolean oldIsImage) {
          this.oldIsImage = oldIsImage;
      }

      public boolean isNewIsImage() {
          return newIsImage;
      }

      public void setNewIsImage(boolean newIsImage) {
          this.newIsImage = newIsImage;
      }

      public String getOldObjectId() {
          return oldObjectId;
      }

      public void setOldObjectId(String oldObjectId) {
          this.oldObjectId = oldObjectId;
      }

      public String getNewObjectId() {
          return newObjectId;
      }

      public void setNewObjectId(String newObjectId) {
          this.newObjectId = newObjectId;
      }

      public String getOldMode() {
          return oldMode;
      }

      public void setOldMode(String oldMode) {
          this.oldMode = oldMode;
      }

      public String getNewMode() {
          return newMode;
      }

      public void setNewMode(String newMode) {
          this.newMode = newMode;
      }

      public boolean isTooLarge() {
          return tooLarge;
      }

      public void setTooLarge(boolean tooLarge) {
          this.tooLarge = tooLarge;
      }

      public String getPatch() {
          return patch;
      }

      public void setPatch(String patch) {
          this.patch = patch;
      }
  }


        public static class ContentInfo {
            protected  String viewType;
            protected  Long size;
            protected  String content;
            protected  String charset;

            public ContentInfo() {
            }

            /**
             * The file content data for the file content view of the repository viewer.
             *
             * @param viewType "image", "large" or "other"
             * @param size total size of object in bytes
             * @param content the string content
             * @param charset the character encoding
             */
            public ContentInfo(String viewType, Long size, String content, String charset) {
                this.viewType = viewType;
                this.size = size;
                this.content = content;
                this.charset = charset;
            }

            /**
         * the line separator of this content ("LF" or "CRLF")
         */
        public String lineSeparator(){
            if(content!=null&&content.indexOf("\r\n")>=0){
                return "CRLF";
            }
            return "LF";
        }

            public String getViewType() {
                return viewType;
            }

            public void setViewType(String viewType) {
                this.viewType = viewType;
            }

            public Long getSize() {
                return size;
            }

            public void setSize(Long size) {
                this.size = size;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getCharset() {
                return charset;
            }

            public void setCharset(String charset) {
                this.charset = charset;
            }
        }


  public static class TagInfo{
        protected  String name;
        protected  Date time;
        protected  String id;
        protected  String message;

        public TagInfo() {
        }

      /**
       * The tag data.
       *
       * @param name the tag name
       * @param time the tagged date
       * @param id the commit id
       * @param message the message of the tagged commit
       */
        public TagInfo(String name, Date time, String id, String message) {
            this.name = name;
            this.time = time;
            this.id = id;
            this.message = message;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }


        public static class SubmoduleInfo{
            protected  String name;
            protected  String path;
            protected  String repositoryUrl;
            protected  String viewerUrl;

            public SubmoduleInfo() {
            }

            /**
             * The submodule data
             *
             * @param name the module name
             * @param path the path in the repository
             * @param repositoryUrl the repository url of this module
             * @param viewerUrl the repository viewer url of this module
             */
            public SubmoduleInfo(String name, String path, String repositoryUrl, String viewerUrl) {
                this.name = name;
                this.path = path;
                this.repositoryUrl = repositoryUrl;
                this.viewerUrl = viewerUrl;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public String getRepositoryUrl() {
                return repositoryUrl;
            }

            public void setRepositoryUrl(String repositoryUrl) {
                this.repositoryUrl = repositoryUrl;
            }

            public String getViewerUrl() {
                return viewerUrl;
            }

            public void setViewerUrl(String viewerUrl) {
                this.viewerUrl = viewerUrl;
            }
        }

        public static class BranchMergeInfo{
            protected  int  ahead;
            protected  int behind;
            protected  boolean isMerged;

            public BranchMergeInfo() {
            }

            public BranchMergeInfo(int ahead, int behind, boolean isMerged) {
                this.ahead = ahead;
                this.behind = behind;
                this.isMerged = isMerged;
            }

            public int getAhead() {
                return ahead;
            }

            public void setAhead(int ahead) {
                this.ahead = ahead;
            }

            public int getBehind() {
                return behind;
            }

            public void setBehind(int behind) {
                this.behind = behind;
            }

            public boolean isMerged() {
                return isMerged;
            }

            public void setMerged(boolean merged) {
                isMerged = merged;
            }
        }

        public static class BranchInfo{

            protected  String  name;
            protected  String  committerName;
            protected  Date commitTime;
            protected  String committerEmailAddress;
            protected  BranchMergeInfo mergeInfo;
            protected  String commitId;

            public BranchInfo() {
            }

            public BranchInfo(String name, String committerName, Date commitTime, String committerEmailAddress, BranchMergeInfo mergeInfo, String commitId) {
                this.name = name;
                this.committerName = committerName;
                this.commitTime = commitTime;
                this.committerEmailAddress = committerEmailAddress;
                this.mergeInfo = mergeInfo;
                this.commitId = commitId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCommitterName() {
                return committerName;
            }

            public void setCommitterName(String committerName) {
                this.committerName = committerName;
            }

            public Date getCommitTime() {
                return commitTime;
            }

            public void setCommitTime(Date commitTime) {
                this.commitTime = commitTime;
            }

            public String getCommitterEmailAddress() {
                return committerEmailAddress;
            }

            public void setCommitterEmailAddress(String committerEmailAddress) {
                this.committerEmailAddress = committerEmailAddress;
            }

            public BranchMergeInfo getMergeInfo() {
                return mergeInfo;
            }

            public void setMergeInfo(BranchMergeInfo mergeInfo) {
                this.mergeInfo = mergeInfo;
            }

            public String getCommitId() {
                return commitId;
            }

            public void setCommitId(String commitId) {
                this.commitId = commitId;
            }
        }


      public static class BlameInfo {
          protected  String  id;
          protected  String authorName;
          protected  String authorEmailAddress;
          protected  Date authorTime;
          protected  String prev;
          protected  String prevPath;
          protected  Date commitTime;
          protected  String message;
          protected Set<Integer> lines;

          public BlameInfo() {
          }

          public BlameInfo(String id, String authorName, String authorEmailAddress, Date authorTime, String prev, String prevPath, Date commitTime, String message, Set<Integer> lines) {
              this.id = id;
              this.authorName = authorName;
              this.authorEmailAddress = authorEmailAddress;
              this.authorTime = authorTime;
              this.prev = prev;
              this.prevPath = prevPath;
              this.commitTime = commitTime;
              this.message = message;
              this.lines = lines;
          }

          public String getId() {
              return id;
          }

          public void setId(String id) {
              this.id = id;
          }

          public String getAuthorName() {
              return authorName;
          }

          public void setAuthorName(String authorName) {
              this.authorName = authorName;
          }

          public String getAuthorEmailAddress() {
              return authorEmailAddress;
          }

          public void setAuthorEmailAddress(String authorEmailAddress) {
              this.authorEmailAddress = authorEmailAddress;
          }

          public Date getAuthorTime() {
              return authorTime;
          }

          public void setAuthorTime(Date authorTime) {
              this.authorTime = authorTime;
          }

          public String getPrev() {
              return prev;
          }

          public void setPrev(String prev) {
              this.prev = prev;
          }

          public String getPrevPath() {
              return prevPath;
          }

          public void setPrevPath(String prevPath) {
              this.prevPath = prevPath;
          }

          public Date getCommitTime() {
              return commitTime;
          }

          public void setCommitTime(Date commitTime) {
              this.commitTime = commitTime;
          }

          public String getMessage() {
              return message;
          }

          public void setMessage(String message) {
              this.message = message;
          }

          public Set<Integer> getLines() {
              return lines;
          }

          public void setLines(Set<Integer> lines) {
              this.lines = lines;
          }
      }

    /**
     * Returns RevCommit from the commit or tag id.
     *
     * @param git the Git object
     * @param objectId the ObjectId of the commit or tag
     * @return the RevCommit for the specified commit or tag
     */
    public static RevCommit getRevCommitFromId(Git git, ObjectId objectId){
        try {
            RevWalk revWalk = new RevWalk(git.getRepository());
            RevObject any = revWalk.parseAny(objectId);
            RevCommit revCommit = null;
            if(any instanceof RevTag){
                revCommit = revWalk.parseCommit(((RevTag) any).getObject());
            }else{
                revCommit = revWalk.parseCommit(objectId);
            }
            return revCommit;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final Cache<String, Integer> cache = new Cache2kBuilder<String,Integer>(){}.name("commit-count")
        .expireAfterWrite(24, TimeUnit.HOURS)
        .entryCapacity(10000)
        .build();


    public static void removeCache(Git git){
        File dir = git.getRepository().getDirectory();
        String keyPrefix = dir.getAbsolutePath() + "@";
        for(String key:cache.keys()){
            if (key.startsWith(keyPrefix)) {
                cache.remove(key);
            }
        }
    }

    /**
     * Returns the number of commits in the specified branch or commit.
     * If the specified branch has over 10000 commits, this method returns 100001.
     */
    public static Integer getCommitCount(String owner,String repository,String branch){
        File dir = getRepositoryDir(owner, repository);
        String key = dir.getAbsolutePath() + "@" + branch;
        CacheEntry<String, Integer> entry = cache.getEntry(key);
        if (entry == null) {
            Git git = gitOpen(dir);
            try {
                ObjectId commitId = git.getRepository().resolve(branch);
                Iterator<RevCommit> it = git.log().add(commitId).call().iterator();
                int max = 10001, commitCount = 0;
                while(it.hasNext()&&max-->0){
                    it.next();
                    commitCount++;
                }
                cache.put(key, commitCount);
                return commitCount;
            } catch (Exception e) {
                throw new RuntimeException(e );
            }
        } else {
            return entry.getValue();
        }
    }

    /**
     * Add by tylerchen
     * @param dir
     * @return
     */
    public static Git gitOpen(File dir){
        return GitHelper.open(dir);
    }

    /**
     * Add by tylerchen
     * @param dir
     * @return
     */
    public static String updateProject(File dir){
        return GitHelper.updateProject(dir);
    }

    /**
     * Returns the repository information. It contains branch names and tag names.
     */
    public static RepositoryInfo getRepositoryInfo(String owner,String repository){
       Git git = gitOpen(getRepositoryDir(owner, repository));
        try {
            List<Ref> refs = git.branchList().call();
            List<String> branchList = new ArrayList<>();
            for(Ref ref:refs){
                branchList.add(StringUtils.remove(ref.getName(), "refs/heads/"));
            }
            List<Ref> tags = git.tagList().call();
            List<TagInfo> tis = new ArrayList<>();
            for(Ref ref:tags){
                RevCommit revCommit = getRevCommitFromId(git, ref.getObjectId());
                TagInfo tagInfo = new TagInfo(StringUtils.remove(revCommit.getName(), "refs/tags/"),
                    revCommit.getCommitterIdent().getWhen(),
                    revCommit.getName(),
                    revCommit.getShortMessage());
                tis.add(tagInfo);
            }
            Collections.sort(tis, new Comparator<TagInfo>() {
                public int compare(TagInfo ti1, TagInfo ti2) {
                    return ti1.getTime().compareTo(ti2.getTime());
                }
            });
            return new RepositoryInfo(
                owner,
                repository,
                // branches
                branchList,
                // tags
                tis
            );
        } catch (Exception e){
            return new RepositoryInfo(owner, repository, null, null);
        }
    }
    private static void useTreeWalk(RevCommit rev, String path, Git git, Function0 f){
        try {
            if (".".equals(path)) {
                TreeWalk treeWalk = new TreeWalk(git.getRepository());
                treeWalk.addTree(rev.getTree());
                //TODO using(treeWalk)(f);
                f.apply();
            } else {
                TreeWalk treeWalk = TreeWalk.forPath(git.getRepository(), path, rev.getTree());
                if (treeWalk != null) {
                    treeWalk.enterSubtree();
                    //TODO using(treeWalk)(f);
                    f.apply();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //ObjectId, FileMode, String, String, Option[String], RevCommit
    private static Object[]  simplifyPath(Git git, ObjectId oid, FileMode fm, String name, String path, String _, RevCommit commit) throws Exception {
        if(fm !=FileMode.TREE) {
            return new Object[]{ oid,  fm,  name,  path,  _,  commit};
        }
        TreeWalk walk = new TreeWalk(git.getRepository());
        walk.addTree(oid);
        // single tree child, or None
        if (walk.next() && walk.getFileMode(0) == FileMode.TREE) {
            return simplifyPath(git, walk.getObjectId(0), walk.getFileMode(0), name + "/" + walk.getNameString(), path + "/" + walk.getNameString(),
                null,commit);
        } else {
            return new Object[]{ oid,  fm,  name,  path,  _,  commit};
        }
    }

    private static Object[] tupleAdd( ObjectId oid, FileMode fm, String name, String path, String _, RevCommit commit){
        return new Object[]{ oid,  fm,  name,  path,  _,  commit};
    }

//    /**
//     * Returns the file list of the specified path.
//     *
//     * @param git the Git object
//     * @param revision the branch name or commit id
//     * @param path the directory path (optional)
//     * @param baseUrl the base url of GitBucket instance. This parameter is used to generate links of submodules (optional)
//     * @return HTML of the file list
//     */
//
//    //result = List[(ObjectId, FileMode, String, String, Option[String], RevCommit)],
//    //restList = List[((ObjectId, FileMode, String, String, Option[String]), Map[RevCommit, RevCommit])],
//    //revIterator = java.util.Iterator[RevCommit]
//    private static List<Object[]> findLastCommits(List<Object[]> result, List<Object[]> restList, Iterator<RevCommit>revIterator, RevCommit revCommit) {
//        if (restList.isEmpty()) {
//            return result;
//        } else if (!revIterator.hasNext()) { // maybe, revCommit has only 1 log. other case, restList be empty
//            List<Object[]> newResult = new ArrayList<>();
//            newResult.addAll(result);
//            for(Object[] objs:restList) {
//                Object[] tuple = (Object[])objs[0];
//                RevCommit next = ((Map<RevCommit, RevCommit>) objs[1]).values().iterator().next();
//                Object[] objects = tupleAdd((ObjectId) tuple[0], (FileMode) tuple[1], (String) tuple[2], (String) tuple[3], (String) tuple[4],
//                    next == null ? revCommit : next);
//                newResult.add(objects);
//            }
//        } else {
//            RevCommit newCommit = revIterator.next();
//            List<Object[]> thisTimeChecks = new ArrayList<>();
//            List<Object[]> skips = new ArrayList<>();
//            for(Object[] objs:restList) {
//                Map<RevCommit, RevCommit> map =(Map<RevCommit, RevCommit> )  objs[1];
//                if(map.containsKey(newCommit)) {
//                    thisTimeChecks.add(objs);
//                }else {
//                    skips.add(objs);
//                }
//            }
//
//            if (thisTimeChecks.isEmpty()) {
//                findLastCommits(result, restList, revIterator, null);
//            } else {
//                List<Object[]> nextRest = skips;
//                List<Object[]> nextResult = result;
//                // Map[(name, oid), (tuple, parentsMap)]
//                val rest = scala.collection.mutable.Map(thisTimeChecks.map { t =>
//                    (t._1._3 -> t._1._1) -> t
//                }: _*)
//                lazy val newParentsMap = newCommit.getParents.map(_ -> newCommit).toMap
//                useTreeWalk(newCommit) { walk =>
//                    while (walk.next) {
//                        rest.remove(walk.getNameString -> walk.getObjectId(0)).foreach {
//                            case (tuple, _) =>
//                                if (newParentsMap.isEmpty) {
//                                    nextResult +:= tupleAdd(tuple, newCommit)
//                                } else {
//                                    nextRest +:= tuple -> newParentsMap
//                                }
//                        }
//                    }
//                }
//                rest.values.foreach {
//                    case (tuple, parentsMap) =>
//                        val restParentsMap = parentsMap - newCommit
//                        if (restParentsMap.isEmpty) {
//                            nextResult +:= tupleAdd(tuple, parentsMap(newCommit))
//                        } else {
//                            nextRest +:= tuple -> restParentsMap
//                        }
//                }
//                findLastCommits(nextResult, nextRest, revIterator)
//            }
//        }
//    }
//
//    public static List<FileInfo> getFileList(Git git,String revision,String path,String baseUrl){
//        path = path ==null?".":path;
//        RevWalk revWalk = new RevWalk(git.getRepository());
//        ObjectId objectId = git.getRepository().resolve(revision);
//        if (objectId == null){return null;}
//        RevCommit revCommit = revWalk.parseCommit(objectId);
//
//
//
//            @tailrec
//            def findLastCommits(
//                result: List[(ObjectId, FileMode, String, String, Option[String], RevCommit)],
//            restList: List[((ObjectId, FileMode, String, String, Option[String]), Map[RevCommit, RevCommit])],
//            revIterator: java.util.Iterator[RevCommit]
//      ): List[(ObjectId, FileMode, String, String, Option[String], RevCommit)] = {
//                if (restList.isEmpty) {
//                    result
//                } else if (!revIterator.hasNext) { // maybe, revCommit has only 1 log. other case, restList be empty
//                    result ++ restList.map { case (tuple, map) => tupleAdd(tuple, map.values.headOption.getOrElse(revCommit)) }
//                } else {
//                    val newCommit = revIterator.next
//                    val (thisTimeChecks, skips) = restList.partition {
//                        case (tuple, parentsMap) => parentsMap.contains(newCommit)
//                    }
//                    if (thisTimeChecks.isEmpty) {
//                        findLastCommits(result, restList, revIterator)
//                    } else {
//                        var nextRest = skips
//                        var nextResult = result
//                        // Map[(name, oid), (tuple, parentsMap)]
//                        val rest = scala.collection.mutable.Map(thisTimeChecks.map { t =>
//                            (t._1._3 -> t._1._1) -> t
//                        }: _*)
//                        lazy val newParentsMap = newCommit.getParents.map(_ -> newCommit).toMap
//                        useTreeWalk(newCommit) { walk =>
//                            while (walk.next) {
//                                rest.remove(walk.getNameString -> walk.getObjectId(0)).foreach {
//                                    case (tuple, _) =>
//                                        if (newParentsMap.isEmpty) {
//                                            nextResult +:= tupleAdd(tuple, newCommit)
//                                        } else {
//                                            nextRest +:= tuple -> newParentsMap
//                                        }
//                                }
//                            }
//                        }
//                        rest.values.foreach {
//                            case (tuple, parentsMap) =>
//                                val restParentsMap = parentsMap - newCommit
//                                if (restParentsMap.isEmpty) {
//                                    nextResult +:= tupleAdd(tuple, parentsMap(newCommit))
//                                } else {
//                                    nextRest +:= tuple -> restParentsMap
//                                }
//                        }
//                        findLastCommits(nextResult, nextRest, revIterator)
//                    }
//                }
//            }
//
//            var fileList: List[(ObjectId, FileMode, String, String, Option[String])] = Nil
//            useTreeWalk(revCommit) { treeWalk =>
//                while (treeWalk.next()) {
//                    val linkUrl = if (treeWalk.getFileMode(0) == FileMode.GITLINK) {
//                        getSubmodules(git, revCommit.getTree, baseUrl).find(_.path == treeWalk.getPathString).map(_.viewerUrl)
//                    } else None
//                    fileList +:= (treeWalk.getObjectId(0), treeWalk.getFileMode(0), treeWalk.getNameString, treeWalk.getPathString, linkUrl)
//                }
//            }
//            revWalk.markStart(revCommit)
//            val it = revWalk.iterator
//            val lastCommit = it.next
//            val nextParentsMap = Option(lastCommit).map(_.getParents.map(_ -> lastCommit).toMap).getOrElse(Map())
//            findLastCommits(List.empty, fileList.map(a => a -> nextParentsMap), it)
//        .map(simplifyPath)
//                .map {
//                case (objectId, fileMode, name, path, linkUrl, commit) =>
//                    FileInfo(
//                        objectId,
//                        fileMode == FileMode.TREE || fileMode == FileMode.GITLINK,
//                        name,
//                        path,
//                        getSummaryMessage(commit.getFullMessage, commit.getShortMessage),
//                        commit.getName,
//                        commit.getAuthorIdent.getWhen,
//                        commit.getAuthorIdent.getName,
//                        commit.getAuthorIdent.getEmailAddress,
//                        linkUrl
//                    )
//            }
//        .sortWith { (file1, file2) =>
//                (file1.isDirectory, file2.isDirectory) match {
//                    case (true, false) => true
//                    case (false, true) => false
//                    case _             => file1.name.compareTo(file2.name) < 0
//                }
//            }
//        }
//    }
//
//    /**
//     * Returns the first line of the commit message.
//     */
//    private def getSummaryMessage(fullMessage: String, shortMessage: String): String = {
//        defining(fullMessage.trim.indexOf('\n')) { i =>
//            defining(if (i >= 0) fullMessage.trim.substring(0, i).trim else fullMessage) { firstLine =>
//                if (firstLine.length > shortMessage.length) shortMessage else firstLine
//            }
//        }
//    }
//
//    /**
//     * get all file list by revision. only file.
//     */
//    def getTreeId(git: Git, revision: String): Option[String] = {
//        using(new RevWalk(git.getRepository)) { revWalk =>
//            val objectId = git.getRepository.resolve(revision)
//            if (objectId == null) return None
//            val revCommit = revWalk.parseCommit(objectId)
//            Some(revCommit.getTree.name)
//        }
//    }
//
//    /**
//     * get all file list by tree object id.
//     */
//    def getAllFileListByTreeId(git: Git, treeId: String): List[String] = {
//        using(new RevWalk(git.getRepository)) { revWalk =>
//            val objectId = git.getRepository.resolve(treeId + "^{tree}")
//            if (objectId == null) return Nil
//            using(new TreeWalk(git.getRepository)) { treeWalk =>
//                treeWalk.addTree(objectId)
//                treeWalk.setRecursive(true)
//                var ret: List[String] = Nil
//                if (treeWalk != null) {
//                    while (treeWalk.next()) {
//                        ret +:= treeWalk.getPathString
//                    }
//                }
//                ret.reverse
//            }
//        }
//    }
//
//    /**
//     * Returns the commit list of the specified branch.
//     *
//     * @param git the Git object
//     * @param revision the branch name or commit id
//     * @param page the page number (1-)
//     * @param limit the number of commit info per page. 0 (default) means unlimited.
//     * @param path filters by this path. default is no filter.
//     * @return a tuple of the commit list and whether has next, or the error message
//     */
//    def getCommitLog(
//        git: Git,
//        revision: String,
//        page: Int = 1,
//        limit: Int = 0,
//        path: String = ""
//    ): Either[String, (List[CommitInfo], Boolean)] = {
//        val fixedPage = if (page <= 0) 1 else page
//
//        @scala.annotation.tailrec
//        def getCommitLog(
//            i: java.util.Iterator[RevCommit],
//            count: Int,
//            logs: List[CommitInfo]
//    ): (List[CommitInfo], Boolean) =
//        i.hasNext match {
//            case true if (limit <= 0 || logs.size < limit) => {
//                val commit = i.next
//                getCommitLog(
//                    i,
//                    count + 1,
//                if (limit <= 0 || (fixedPage - 1) * limit <= count) logs :+ new CommitInfo(commit) else logs
//          )
//            }
//            case _ => (logs, i.hasNext)
//        }
//
//        using(new RevWalk(git.getRepository)) { revWalk =>
//            defining(git.getRepository.resolve(revision)) { objectId =>
//                if (objectId == null) {
//                    Left(s"${revision} can't be resolved.")
//                } else {
//                    revWalk.markStart(revWalk.parseCommit(objectId))
//                    if (path.nonEmpty) {
//                        revWalk.setTreeFilter(AndTreeFilter.create(PathFilter.create(path), TreeFilter.ANY_DIFF))
//                    }
//                    Right(getCommitLog(revWalk.iterator, 0, Nil))
//                }
//            }
//        }
//    }
//
//    def getCommitLogs(git: Git, begin: String, includesLastCommit: Boolean = false)(
//    endCondition: RevCommit => Boolean
//  ): List[CommitInfo] = {
//        @scala.annotation.tailrec
//        def getCommitLog(i: java.util.Iterator[RevCommit], logs: List[CommitInfo]): List[CommitInfo] =
//            i.hasNext match {
//            case true => {
//                val revCommit = i.next
//                if (endCondition(revCommit)) {
//                    if (includesLastCommit) logs :+ new CommitInfo(revCommit) else logs
//                } else {
//                    getCommitLog(i, logs :+ new CommitInfo(revCommit))
//                }
//            }
//            case false => logs
//        }
//
//        using(new RevWalk(git.getRepository)) { revWalk =>
//            revWalk.markStart(revWalk.parseCommit(git.getRepository.resolve(begin)))
//            getCommitLog(revWalk.iterator, Nil).reverse
//        }
//    }
//
//    /**
//     * Returns the commit list between two revisions.
//     *
//     * @param git the Git object
//     * @param from the from revision
//     * @param to the to revision
//     * @return the commit list
//     */
//    // TODO swap parameters 'from' and 'to'!?
//    def getCommitLog(git: Git, from: String, to: String): List[CommitInfo] =
//    getCommitLogs(git, to)(_.getName == from)
//
//    /**
//     * Returns the latest RevCommit of the specified path.
//     *
//     * @param git the Git object
//     * @param path the path
//     * @param revision the branch name or commit id
//     * @return the latest commit
//     */
//    def getLatestCommitFromPath(git: Git, path: String, revision: String): Option[RevCommit] =
//    getLatestCommitFromPaths(git, List(path), revision).get(path)
//
//    /**
//     * Returns the list of latest RevCommit of the specified paths.
//     *
//     * @param git the Git object
//     * @param paths the list of paths
//     * @param revision the branch name or commit id
//     * @return the list of latest commit
//     */
//    def getLatestCommitFromPaths(git: Git, paths: List[String], revision: String): Map[String, RevCommit] = {
//        val start = getRevCommitFromId(git, git.getRepository.resolve(revision))
//        paths.map { path =>
//            val commit = git.log.add(start).addPath(path).setMaxCount(1).call.iterator.next
//                (path, commit)
//        }.toMap
//    }
//
//    def getPatch(git: Git, from: Option[String], to: String): String = {
//        val out = new ByteArrayOutputStream()
//        val df = new DiffFormatter(out)
//        df.setRepository(git.getRepository)
//        df.setDiffComparator(RawTextComparator.DEFAULT)
//        df.setDetectRenames(true)
//        getDiffEntries(git, from, to)
//            .map { entry =>
//            df.format(entry)
//            new String(out.toByteArray, "UTF-8")
//        }
//      .mkString("\n")
//    }
//
//    private def getDiffEntries(git: Git, from: Option[String], to: String): Seq[DiffEntry] = {
//        using(new RevWalk(git.getRepository)) { revWalk =>
//            val df = new DiffFormatter(DisabledOutputStream.INSTANCE)
//            df.setRepository(git.getRepository)
//
//            val toCommit = revWalk.parseCommit(git.getRepository.resolve(to))
//            from match {
//                case None => {
//                    toCommit.getParentCount match {
//                        case 0 =>
//                            df.scan(
//                                new EmptyTreeIterator(),
//                                new CanonicalTreeParser(null, git.getRepository.newObjectReader(), toCommit.getTree)
//                            )
//                                .asScala
//                        case _ => df.scan(toCommit.getParent(0), toCommit.getTree).asScala
//                    }
//                }
//                case Some(from) => {
//                    val fromCommit = revWalk.parseCommit(git.getRepository.resolve(from))
//                    df.scan(fromCommit.getTree, toCommit.getTree).asScala
//                }
//            }
//        }
//    }
//
//    def getParentCommitId(git: Git, id: String): Option[String] = {
//        using(new RevWalk(git.getRepository)) { revWalk =>
//            val commit = revWalk.parseCommit(git.getRepository.resolve(id))
//            commit.getParentCount match {
//                case 0 => None
//                case _ => Some(commit.getParent(0).getName)
//            }
//        }
//    }
//
//    def getDiffs(
//        git: Git,
//        from: Option[String],
//        to: String,
//        fetchContent: Boolean,
//        makePatch: Boolean
//    ): List[DiffInfo] = {
//        val diffs = getDiffEntries(git, from, to)
//        diffs.map { diff =>
//            if (diffs.size > 100) {
//                DiffInfo(
//                    changeType = diff.getChangeType,
//                    oldPath = diff.getOldPath,
//                    newPath = diff.getNewPath,
//                    oldContent = None,
//                    newContent = None,
//                    oldIsImage = false,
//                    newIsImage = false,
//                    oldObjectId = Option(diff.getOldId).map(_.name),
//                    newObjectId = Option(diff.getNewId).map(_.name),
//                    oldMode = diff.getOldMode.toString,
//                    newMode = diff.getNewMode.toString,
//                    tooLarge = true,
//                    patch = None
//                )
//            } else {
//                val oldIsImage = FileUtil.isImage(diff.getOldPath)
//                val newIsImage = FileUtil.isImage(diff.getNewPath)
//                if (!fetchContent || oldIsImage || newIsImage) {
//                    DiffInfo(
//                        changeType = diff.getChangeType,
//                        oldPath = diff.getOldPath,
//                        newPath = diff.getNewPath,
//                        oldContent = None,
//                        newContent = None,
//                        oldIsImage = oldIsImage,
//                        newIsImage = newIsImage,
//                        oldObjectId = Option(diff.getOldId).map(_.name),
//                        newObjectId = Option(diff.getNewId).map(_.name),
//                        oldMode = diff.getOldMode.toString,
//                        newMode = diff.getNewMode.toString,
//                        tooLarge = false,
//                        patch = (if (makePatch) Some(makePatchFromDiffEntry(git, diff)) else None) // TODO use DiffFormatter
//          )
//                } else {
//                    DiffInfo(
//                        changeType = diff.getChangeType,
//                        oldPath = diff.getOldPath,
//                        newPath = diff.getNewPath,
//                        oldContent = JGitUtil
//                            .getContentFromId(git, diff.getOldId.toObjectId, false)
//                            .filter(FileUtil.isText)
//                            .map(convertFromByteArray),
//                        newContent = JGitUtil
//                            .getContentFromId(git, diff.getNewId.toObjectId, false)
//                            .filter(FileUtil.isText)
//                            .map(convertFromByteArray),
//                        oldIsImage = oldIsImage,
//                        newIsImage = newIsImage,
//                        oldObjectId = Option(diff.getOldId).map(_.name),
//                        newObjectId = Option(diff.getNewId).map(_.name),
//                        oldMode = diff.getOldMode.toString,
//                        newMode = diff.getNewMode.toString,
//                        tooLarge = false,
//                        patch = (if (makePatch) Some(makePatchFromDiffEntry(git, diff)) else None) // TODO use DiffFormatter
//          )
//                }
//            }
//        }.toList
//    }
//
//    private def makePatchFromDiffEntry(git: Git, diff: DiffEntry): String = {
//        val out = new ByteArrayOutputStream()
//        using(new DiffFormatter(out)) { formatter =>
//            formatter.setRepository(git.getRepository)
//            formatter.format(diff)
//            val patch = new String(out.toByteArray) // TODO charset???
//            patch.split("\n").drop(4).mkString("\n")
//        }
//    }
//
//    /**
//     * Returns the list of branch names of the specified commit.
//     */
//    def getBranchesOfCommit(git: Git, commitId: String): List[String] =
//    using(new RevWalk(git.getRepository)) { revWalk =>
//        defining(revWalk.parseCommit(git.getRepository.resolve(commitId + "^0"))) { commit =>
//            git.getRepository.getRefDatabase
//                .getRefsByPrefix(Constants.R_HEADS)
//                .asScala
//                .filter { e =>
//                (revWalk.isMergedInto(
//                    commit,
//                    revWalk.parseCommit(e.getObjectId)
//                ))
//            }
//          .map { e =>
//                e.getName.substring(Constants.R_HEADS.length)
//            }
//          .toList
//                .sorted
//        }
//    }
//
//    /**
//     * Returns the list of tags which pointed on the specified commit.
//     */
//    def getTagsOnCommit(git: Git, commitId: String): List[String] = {
//        git.getRepository.getAllRefsByPeeledObjectId.asScala
//            .get(git.getRepository.resolve(commitId + "^0"))
//            .map {
//            _.asScala
//                .collect {
//                case x if x.getName.startsWith(Constants.R_TAGS) =>
//                    x.getName.substring(Constants.R_TAGS.length)
//            }
//          .toList
//                .sorted
//        }
//      .getOrElse {
//            List.empty
//        }
//    }
//
//    /**
//     * Returns the list of tags which contains the specified commit.
//     */
//    def getTagsOfCommit(git: Git, commitId: String): List[String] =
//    using(new RevWalk(git.getRepository)) { revWalk =>
//        defining(revWalk.parseCommit(git.getRepository.resolve(commitId + "^0"))) { commit =>
//            git.getRepository.getRefDatabase
//                .getRefsByPrefix(Constants.R_TAGS)
//                .asScala
//                .filter { e =>
//                (revWalk.isMergedInto(
//                    commit,
//                    revWalk.parseCommit(e.getObjectId)
//                ))
//            }
//          .map { e =>
//                e.getName.substring(Constants.R_TAGS.length)
//            }
//          .toList
//                .sorted
//                .reverse
//        }
//    }
//
//    //def initRepository(dir: java.io.File): Unit = {
//    def initRepository(dir: java.io.File): Unit =
//    using(new RepositoryBuilder().setGitDir(dir).setBare.build) { repository =>
//        repository.create(true)
//        setReceivePack(repository)
//    }
//    //gitbucket.core.util.GitHelper.initRepository(dir)
//    //}
//
//    def cloneRepository(from: java.io.File, to: java.io.File): Unit =
//    using(Git.cloneRepository.setURI(from.toURI.toString).setDirectory(to).setBare(true).call) { git =>
//        setReceivePack(git.getRepository)
//    }
//
//    def isEmpty(git: Git): Boolean = git.getRepository.resolve(Constants.HEAD) == null
//
//    private def setReceivePack(repository: org.eclipse.jgit.lib.Repository): Unit =
//    defining(repository.getConfig) { config =>
//        config.setBoolean("http", null, "receivepack", true)
//        config.save
//    }
//
//    def getDefaultBranch(
//        git: Git,
//        repository: RepositoryService.RepositoryInfo,
//        revstr: String = ""
//    ): Option[(ObjectId, String)] = {
//        Seq(
//            Some(if (revstr.isEmpty) repository.repository.defaultBranch else revstr),
//        repository.branchList.headOption
//    ).flatMap {
//            case Some(rev) => Some((git.getRepository.resolve(rev), rev))
//            case None      => None
//        }
//      .find(_._1 != null)
//    }
//
//    def createTag(git: Git, name: String, message: Option[String], commitId: String) = {
//        try {
//            val objectId: ObjectId = git.getRepository.resolve(commitId)
//            using(new RevWalk(git.getRepository)) { walk =>
//                val tagCommand = git.tag().setName(name).setObjectId(walk.parseCommit(objectId))
//                message.foreach { message =>
//                    tagCommand.setMessage(message)
//                }
//                tagCommand.call()
//            }
//            Right("Tag added.")
//        } catch {
//            case e: GitAPIException              => Left("Sorry, some Git operation error occurs.")
//            case e: ConcurrentRefUpdateException => Left("Sorry some error occurs.")
//            case e: InvalidTagNameException      => Left("Sorry, that name is invalid.")
//            case e: NoHeadException              => Left("Sorry, this repo doesn't have HEAD reference")
//        }
//    }
//
//    def createBranch(git: Git, fromBranch: String, newBranch: String) = {
//        try {
//            git.branchCreate().setStartPoint(fromBranch).setName(newBranch).call()
//            Right("Branch created.")
//        } catch {
//            case e: RefAlreadyExistsException => Left("Sorry, that branch already exists.")
//                // JGitInternalException occurs when new branch name is 'a' and the branch whose name is 'a/*' exists.
//            case _: InvalidRefNameException | _: JGitInternalException => Left("Sorry, that name is invalid.")
//        }
//    }
//
//    def createDirCacheEntry(path: String, mode: FileMode, objectId: ObjectId): DirCacheEntry = {
//        val entry = new DirCacheEntry(path)
//        entry.setFileMode(mode)
//        entry.setObjectId(objectId)
//        entry
//    }
//
//    def createNewCommit(
//        git: Git,
//        inserter: ObjectInserter,
//        headId: AnyObjectId,
//        treeId: AnyObjectId,
//        ref: String,
//        fullName: String,
//        mailAddress: String,
//        message: String
//    ): ObjectId = {
//        val newCommit = new CommitBuilder()
//        newCommit.setCommitter(new PersonIdent(fullName, mailAddress))
//        newCommit.setAuthor(new PersonIdent(fullName, mailAddress))
//        newCommit.setMessage(message)
//        if (headId != null) {
//            newCommit.setParentIds(List(headId).asJava)
//        }
//        newCommit.setTreeId(treeId)
//
//        val newHeadId = inserter.insert(newCommit)
//        inserter.flush()
//        inserter.close()
//
//        val refUpdate = git.getRepository.updateRef(ref)
//        refUpdate.setNewObjectId(newHeadId)
//        refUpdate.update()
//
//        removeCache(git)
//
//        newHeadId
//    }
//
//    /**
//     * Read submodule information from .gitmodules
//     */
//    def getSubmodules(git: Git, tree: RevTree, baseUrl: Option[String]): List[SubmoduleInfo] = {
//        val repository = git.getRepository
//        getContentFromPath(git, tree, ".gitmodules", true).map { bytes =>
//            (try {
//                val config = new BlobBasedConfig(repository.getConfig(), bytes)
//                config.getSubsections("submodule").asScala.map { module =>
//                    val path = config.getString("submodule", module, "path")
//                    val url = config.getString("submodule", module, "url")
//                    SubmoduleInfo(module, path, url, StringUtil.getRepositoryViewerUrl(url, baseUrl.get))
//                }
//            } catch {
//                case e: ConfigInvalidException => {
//                    logger.error("Failed to load .gitmodules file for " + repository.getDirectory(), e)
//                    Nil
//                }
//            }).toList
//        } getOrElse Nil
//    }
//
//    /**
//     * Get object content of the given path as byte array from the Git repository.
//     *
//     * @param git the Git object
//     * @param revTree the rev tree
//     * @param path the path
//     * @param fetchLargeFile if false then returns None for the large file
//     * @return the byte array of content or None if object does not exist
//     */
//    def getContentFromPath(git: Git, revTree: RevTree, path: String, fetchLargeFile: Boolean): Option[Array[Byte]] = {
//        @scala.annotation.tailrec
//        def getPathObjectId(path: String, walk: TreeWalk): Option[ObjectId] = walk.next match {
//            case true if (walk.getPathString == path) => Some(walk.getObjectId(0))
//            case true                                 => getPathObjectId(path, walk)
//            case false                                => None
//        }
//
//        using(new TreeWalk(git.getRepository)) { treeWalk =>
//            treeWalk.addTree(revTree)
//            treeWalk.setRecursive(true)
//            getPathObjectId(path, treeWalk)
//        } flatMap { objectId =>
//            getContentFromId(git, objectId, fetchLargeFile)
//        }
//    }
//
//    def getLfsObjects(text: String): Map[String, String] = {
//        if (text.startsWith("version https://git-lfs.github.com/spec/v1")) {
//            // LFS objects
//            text
//                .split("\n")
//                .map { line =>
//                val dim = line.split(" ")
//                dim(0) -> dim(1)
//            }
//        .toMap
//        } else {
//            Map.empty
//        }
//    }
//
//    def getContentSize(loader: ObjectLoader): Long = {
//        if (loader.isLarge) {
//            loader.getSize
//        } else {
//            val bytes = loader.getCachedBytes
//            val text = new String(bytes, "UTF-8")
//
//            val attr = getLfsObjects(text)
//            attr.get("size") match {
//                case Some(size) => size.toLong
//                case None       => loader.getSize
//            }
//        }
//    }
//
//    def isLfsPointer(loader: ObjectLoader): Boolean = {
//        !loader.isLarge && new String(loader.getBytes(), "UTF-8").startsWith("version https://git-lfs.github.com/spec/v1")
//    }
//
//    def getContentInfo(git: Git, path: String, objectId: ObjectId): ContentInfo = {
//        // Viewer
//        using(git.getRepository.getObjectDatabase) { db =>
//            val loader = db.open(objectId)
//            val isLfs = isLfsPointer(loader)
//            val large = FileUtil.isLarge(loader.getSize)
//            val viewer = if (FileUtil.isImage(path)) "image" else if (large) "large" else "other"
//            val bytes = if (viewer == "other") JGitUtil.getContentFromId(git, objectId, false) else None
//            val size = Some(getContentSize(loader))
//
//            if (viewer == "other") {
//                if (!isLfs && bytes.isDefined && FileUtil.isText(bytes.get)) {
//                    // text
//                    ContentInfo(
//                        "text",
//                        size,
//                        Some(StringUtil.convertFromByteArray(bytes.get)),
//                        Some(StringUtil.detectEncoding(bytes.get))
//                    )
//                } else {
//                    // binary
//                    ContentInfo("binary", size, None, None)
//                }
//            } else {
//                // image or large
//                ContentInfo(viewer, size, None, None)
//            }
//        }
//    }
//
//    /**
//     * Get object content of the given object id as byte array from the Git repository.
//     *
//     * @param git the Git object
//     * @param id the object id
//     * @param fetchLargeFile if false then returns None for the large file
//     * @return the byte array of content or None if object does not exist
//     */
//    def getContentFromId(git: Git, id: ObjectId, fetchLargeFile: Boolean): Option[Array[Byte]] =
//        try {
//        using(git.getRepository.getObjectDatabase) { db =>
//            val loader = db.open(id)
//            if (loader.isLarge || (fetchLargeFile == false && FileUtil.isLarge(loader.getSize))) {
//                None
//            } else {
//                Some(loader.getBytes)
//            }
//        }
//    } catch {
//        case e: MissingObjectException => None
//    }
//
//    /**
//     * Get objectLoader of the given object id from the Git repository.
//     *
//     * @param git the Git object
//     * @param id the object id
//     * @param f the function process ObjectLoader
//     * @return None if object does not exist
//     */
//    def getObjectLoaderFromId[A](git: Git, id: ObjectId)(f: ObjectLoader => A): Option[A] =
//        try {
//        using(git.getRepository.getObjectDatabase) { db =>
//            Some(f(db.open(id)))
//        }
//    } catch {
//        case e: MissingObjectException => None
//    }
//
//    /**
//     * Returns all commit id in the specified repository.
//     */
//    def getAllCommitIds(git: Git): Seq[String] =
//        if (isEmpty(git)) {
//        Nil
//    } else {
//        val existIds = new scala.collection.mutable.ListBuffer[String]()
//        val i = git.log.all.call.iterator
//        while (i.hasNext) {
//            existIds += i.next.name
//        }
//        existIds.toSeq
//    }
//
//    def processTree[T](git: Git, id: ObjectId)(f: (String, CanonicalTreeParser) => T): Seq[T] = {
//        using(new RevWalk(git.getRepository)) { revWalk =>
//            using(new TreeWalk(git.getRepository)) { treeWalk =>
//                val index = treeWalk.addTree(revWalk.parseTree(id))
//                treeWalk.setRecursive(true)
//                val result = new collection.mutable.ListBuffer[T]()
//                while (treeWalk.next) {
//                    result += f(treeWalk.getPathString, treeWalk.getTree(index, classOf[CanonicalTreeParser]))
//                }
//                result.toSeq
//            }
//        }
//    }
//
//    /**
//     * Returns the identifier of the root commit (or latest merge commit) of the specified branch.
//     */
//    def getForkedCommitId(
//        oldGit: Git,
//        newGit: Git,
//        userName: String,
//        repositoryName: String,
//        branch: String,
//        requestUserName: String,
//        requestRepositoryName: String,
//        requestBranch: String
//    ): String =
//    defining(getAllCommitIds(oldGit)) { existIds =>
//        getCommitLogs(newGit, requestBranch, true) { commit =>
//            existIds.contains(commit.name) && getBranchesOfCommit(oldGit, commit.getName).contains(branch)
//        }.head.id
//    }
//
//    /**
//     * Fetch pull request contents into refs/pull/${issueId}/head and return (commitIdTo, commitIdFrom)
//     */
//    def updatePullRequest(
//        userName: String,
//        repositoryName: String,
//        branch: String,
//        issueId: Int,
//        requestUserName: String,
//        requestRepositoryName: String,
//        requestBranch: String
//    ): (String, String) =
//    using(
//        gitOpen(Directory.getRepositoryDir(userName, repositoryName)),
//    gitOpen(Directory.getRepositoryDir(requestUserName, requestRepositoryName))
//        ) { (oldGit, newGit) =>
//        oldGit.fetch
//            .setRemote(Directory.getRepositoryDir(requestUserName, requestRepositoryName).toURI.toString)
//            .setRefSpecs(new RefSpec(s"refs/heads/${requestBranch}:refs/pull/${issueId}/head").setForceUpdate(true))
//            .call
//
//        val commitIdTo = oldGit.getRepository.resolve(s"refs/pull/${issueId}/head").getName
//        val commitIdFrom = getForkedCommitId(
//            oldGit,
//            newGit,
//            userName,
//            repositoryName,
//            branch,
//            requestUserName,
//            requestRepositoryName,
//            requestBranch
//        )
//        (commitIdTo, commitIdFrom)
//    }
//
//    /**
//     * Returns the last modified commit of specified path
//     *
//     * @param git the Git object
//     * @param startCommit the search base commit id
//     * @param path the path of target file or directory
//     * @return the last modified commit of specified path
//     */
//    def getLastModifiedCommit(git: Git, startCommit: RevCommit, path: String): RevCommit = {
//        git.log.add(startCommit).addPath(path).setMaxCount(1).call.iterator.next
//    }
//
//    def getBranches(owner: String, name: String, defaultBranch: String, origin: Boolean): Seq[BranchInfo] = {
//        using(gitOpen(getRepositoryDir(owner, name))) { git =>
//            val repo = git.getRepository
//            val defaultObject = repo.resolve(defaultBranch)
//
//            git.branchList.call.asScala.map { ref =>
//                val walk = new RevWalk(repo)
//                try {
//                    val defaultCommit = walk.parseCommit(defaultObject)
//                    val branchName = ref.getName.stripPrefix("refs/heads/")
//                    val branchCommit = walk.parseCommit(ref.getObjectId)
//                    val when = branchCommit.getCommitterIdent.getWhen
//                    val committer = branchCommit.getCommitterIdent.getName
//                    val committerEmail = branchCommit.getCommitterIdent.getEmailAddress
//                    val mergeInfo = if (origin && branchName == defaultBranch) {
//                        None
//                    } else {
//                        walk.reset()
//                        walk.setRevFilter(RevFilter.MERGE_BASE)
//                        walk.markStart(branchCommit)
//                        walk.markStart(defaultCommit)
//                        val mergeBase = walk.next()
//                        walk.reset()
//                        walk.setRevFilter(RevFilter.ALL)
//                        Some(
//                            BranchMergeInfo(
//                                ahead = RevWalkUtils.count(walk, branchCommit, mergeBase),
//                                behind = RevWalkUtils.count(walk, defaultCommit, mergeBase),
//                                isMerged = walk.isMergedInto(branchCommit, defaultCommit)
//                            )
//                        )
//                    }
//                    BranchInfo(branchName, committer, when, committerEmail, mergeInfo, ref.getObjectId.name)
//                } finally {
//                    walk.dispose()
//                }
//            }
//        }
//    }
//
//    def getBlame(git: Git, id: String, path: String): Iterable[BlameInfo] = {
//        Option(git.getRepository.resolve(id))
//            .map { commitId =>
//            val blamer = new org.eclipse.jgit.api.BlameCommand(git.getRepository)
//            blamer.setStartCommit(commitId)
//            blamer.setFilePath(path)
//            val blame = blamer.call()
//            var blameMap = Map[String, JGitUtil.BlameInfo]()
//            var idLine = List[(String, Int)]()
//            val commits = 0.to(blame.getResultContents().size() - 1).map { i =>
//                val c = blame.getSourceCommit(i)
//                if (!blameMap.contains(c.name)) {
//                    blameMap += c.name -> JGitUtil.BlameInfo(
//                        c.name,
//                        c.getAuthorIdent.getName,
//                        c.getAuthorIdent.getEmailAddress,
//                        c.getAuthorIdent.getWhen,
//                        Option(git.log.add(c).addPath(blame.getSourcePath(i)).setSkip(1).setMaxCount(2).call.iterator.next)
//                            .map(_.name),
//                    if (blame.getSourcePath(i) == path) { None } else { Some(blame.getSourcePath(i)) },
//                    c.getCommitterIdent.getWhen,
//                        c.getShortMessage,
//                        Set.empty
//            )
//                }
//                idLine :+= (c.name, i)
//            }
//            val limeMap = idLine.groupBy(_._1).mapValues(_.map(_._2).toSet)
//            blameMap.values.map { b =>
//                b.copy(lines = limeMap(b.id))
//            }
//        }
//      .getOrElse(Seq.empty)
//    }
//
//    /**
//     * Returns sha1
//     *
//     * @param owner repository owner
//     * @param name  repository name
//     * @param revstr  A git object references expression
//     * @return sha1
//     */
//    def getShaByRef(owner: String, name: String, revstr: String): Option[String] = {
//        using(gitOpen(getRepositoryDir(owner, name))) { git =>
//            Option(git.getRepository.resolve(revstr)).map(ObjectId.toString(_))
//        }
//    }
//
//    def getFileSize(git: Git, repository: RepositoryService.RepositoryInfo, treeWalk: TreeWalk): Long = {
//        val attrs = treeWalk.getAttributes
//        val loader = git.getRepository.open(treeWalk.getObjectId(0))
//        if (attrs.containsKey("filter") && attrs.get("filter").getValue == "lfs") {
//            val lfsAttrs = getLfsAttributes(loader)
//            lfsAttrs.get("size").map(_.toLong).get
//        } else {
//            loader.getSize
//        }
//    }
//
//    def getFileSize(git: Git, repository: RepositoryService.RepositoryInfo, tree: RevTree, path: String): Long = {
//        using(TreeWalk.forPath(git.getRepository, path, tree)) { treeWalk =>
//            getFileSize(git, repository, treeWalk)
//        }
//    }
//
//    def openFile[T](git: Git, repository: RepositoryService.RepositoryInfo, treeWalk: TreeWalk)(
//    f: InputStream => T
//  ): T = {
//        val attrs = treeWalk.getAttributes
//        val loader = git.getRepository.open(treeWalk.getObjectId(0))
//        if (attrs.containsKey("filter") && attrs.get("filter").getValue == "lfs") {
//            val lfsAttrs = getLfsAttributes(loader)
//            if (lfsAttrs.nonEmpty) {
//                val oid = lfsAttrs("oid").split(":")(1)
//
//                val file = new File(FileUtil.getLfsFilePath(repository.owner, repository.name, oid))
//                using(new FileInputStream(FileUtil.getLfsFilePath(repository.owner, repository.name, oid))) { in =>
//                    f(in)
//                }
//            } else {
//                throw new NoSuchElementException("LFS attribute is empty.")
//            }
//        } else {
//            using(loader.openStream()) { in =>
//                f(in)
//            }
//        }
//    }
//
//    def openFile[T](git: Git, repository: RepositoryService.RepositoryInfo, tree: RevTree, path: String)(
//    f: InputStream => T
//  ): T = {
//        using(TreeWalk.forPath(git.getRepository, path, tree)) { treeWalk =>
//            openFile(git, repository, treeWalk)(f)
//        }
//    }
//
//    private static Map<String,String> getLfsAttributes(ObjectLoader loader){
//        byte[] bytes = loader.getCachedBytes()
//        String text = new String(bytes, "UTF-8");
//
//        return JGitUtil.getLfsObjects(text);
//    }
}
