package org.hitchain.hit.util;

import java.io.File;
import java.util.Iterator;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.revwalk.RevCommit;

import gitbucket.core.util.GitHelper;

/**
 * Hit utils.
 * @author zhaochen
 */
public class HitUtil {

	public static void main(String[] args) {
		getGitCommit(new File("/Users/zhaochen/Desktop/temppath/jgit/hello.git"));
	}

	public static void getGitCommit(File projectDir) {
		try {
			Git git = GitHelper.open(projectDir);
			Iterator<RevCommit> it = git.log().all().call().iterator();
			while(it.hasNext()) {
				RevCommit next = it.next();
				System.out.println(next.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
