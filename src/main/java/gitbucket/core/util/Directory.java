package gitbucket.core.util;

import java.io.File;

public class Directory {
	public static final String GitBucketHome = getGitBucketHome();
	public static final File GitBucketConf = new File(GitBucketHome, "gitbucket.conf");
	public static final String RepositoryHome = GitBucketHome + "/repositories";
	public static final String DatabaseHome = GitBucketHome + "/data";
	public static final String PluginHome = GitBucketHome + "/plugins";
	public static final String TemporaryHome = GitBucketHome + "/tmp";

	public static String getGitBucketHome() {
		{
			String path = System.getProperty("gitbucket.home", System.getenv("GITBUCKET_HOME"));
			if (path != null) {
				return new File(path).getAbsolutePath();
			}
		}
		{
			File oldHome = new File(System.getProperty("user.home"), "gitbucket");
			if (oldHome.exists() && oldHome.isDirectory() && new File(oldHome, "version").exists()) {
				return oldHome.getAbsolutePath();
			} else {
				return new File(System.getProperty("user.home"), ".gitbucket").getAbsolutePath();
			}
		}
	}

	/**
	 * Substance directory of the repository.
	 */
	public static File getRepositoryDir(String owner, String repository) {
		return new File(RepositoryHome + "/" + owner + "/" + repository + ".git");
	}

	/**
	 * Directory for repository files.
	 */
	public static File getRepositoryFilesDir(String owner, String repository) {
		return new File(RepositoryHome + "/" + owner + "/" + repository);
	}

	/**
	 * Directory for files which are attached to issue.
	 */
	public static File getAttachedDir(String owner, String repository) {
		return new File(getRepositoryFilesDir(owner, repository), "comments");
	}

	/**
	 * Directory for released files
	 */
	public static File getReleaseFilesDir(String owner, String repository) {
		return new File(getRepositoryFilesDir(owner, repository), "releases");
	}

	/**
	 * Directory for files which are attached to issue.
	 */
	public static File getLfsDir(String owner, String repository) {
		return new File(getRepositoryFilesDir(owner, repository), "lfs");
	}

	/**
	 * Directory for files which store diff fragment
	 */
	public static File getDiffDir(String owner, String repository) {
		return new File(getRepositoryFilesDir(owner, repository), "diff");
	}

	/**
	 * Directory for uploaded files by the specified user.
	 */
	public static File getUserUploadDir(String userName) {
		return new File(RepositoryHome + "/data/" + userName + "/files");
	}

	/**
	 * Root of temporary directories for the upload file.
	 */
	public static File getTemporaryDir(String sessionId) {
		return new File(TemporaryHome + "/_upload/" + sessionId);
	}

	/**
	 * Root of temporary directories for the specified repository.
	 */
	public static File getTemporaryDir(String owner, String repository) {
		return new File(TemporaryHome + "/" + owner + "/" + repository);
	}

	/**
	 * Root of plugin cache directory. Plugin repositories are cloned into this
	 * directory.
	 */
	public static File getPluginCacheDir() {
		return new File(TemporaryHome + "/_plugins");
	}

	/**
	 * Substance directory of the wiki repository.
	 */
	public static File getWikiRepositoryDir(String owner, String repository) {
		return new File(RepositoryHome + "/" + owner + "/" + repository + ".wiki.git");
	}
}
