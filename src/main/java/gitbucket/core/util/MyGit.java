/*******************************************************************************
 * Copyright (c) 2018-10-24 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform.
 ******************************************************************************/
package gitbucket.core.util;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.util.FS;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;

/**
 * MyGit
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-10-24
 * auto generate by qdp.
 */
public class MyGit extends Git {

    protected static Constructor<Git> constructor;
    protected final Repository repo;
    protected final boolean closeRepo;
    protected Git target;

    public MyGit(Repository repo) {
        super(repo);
        this.repo = repo;
        this.closeRepo = false;
    }

    public MyGit(Repository repo, boolean closeRepo) {
        super(repo);
        this.repo = repo;
        this.closeRepo = closeRepo;
    }

    public static MyGit open(File dir) throws IOException {
        return open(dir, FS.DETECTED);
    }

    public static MyGit open(File dir, FS fs) throws IOException {
        RepositoryCache.FileKey key;
        key = RepositoryCache.FileKey.lenient(dir, fs);
        Repository db = new RepositoryBuilder().setFS(fs).setGitDir(key.getFile()).setMustExist(true).build();
        if (constructor == null) {
            try {
                constructor = Git.class.getDeclaredConstructor(Repository.class, boolean.class);
                constructor.setAccessible(true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        Git git = null;
        try {
            git = constructor.newInstance(db, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new MyGit(db, true).proxy(git);
    }

    public static MyGit wrap(Repository repo) {
        return new MyGit(repo);
    }

    public static CloneCommand cloneRepository() {
        return Git.cloneRepository();
    }

    public static LsRemoteCommand lsRemoteRepository() {
        return Git.lsRemoteRepository();
    }

    public static InitCommand init() {
        return Git.init();
    }

    protected MyGit proxy(Git git) {
        this.target = git;
        return this;
    }

    public void close() {
        System.out.println("======GIT close=======");
        super.close();
    }

    public CommitCommand commit() {
        return target != null ? target.commit() : super.commit();
    }

    public LogCommand log() {
        return target != null ? target.log() : super.log();
    }

    public MergeCommand merge() {
        return target != null ? target.merge() : super.merge();
    }

    public PullCommand pull() {
        return target != null ? target.pull() : super.pull();
    }

    public CreateBranchCommand branchCreate() {
        return target != null ? target.branchCreate() : super.branchCreate();
    }

    public DeleteBranchCommand branchDelete() {
        return target != null ? target.branchDelete() : super.branchDelete();
    }

    public ListBranchCommand branchList() {
        return target != null ? target.branchList() : super.branchList();
    }

    public ListTagCommand tagList() {
        return target != null ? target.tagList() : super.tagList();
    }

    public RenameBranchCommand branchRename() {
        return target != null ? target.branchRename() : super.branchRename();
    }

    public AddCommand add() {
        return target != null ? target.add() : super.add();
    }

    public TagCommand tag() {
        return target != null ? target.tag() : super.tag();
    }

    public FetchCommand fetch() {
        return target != null ? target.fetch() : super.fetch();
    }

    public PushCommand push() {
        return target != null ? target.push() : super.push();
    }

    public CherryPickCommand cherryPick() {
        return target != null ? target.cherryPick() : super.cherryPick();
    }

    public RevertCommand revert() {
        return target != null ? target.revert() : super.revert();
    }

    public RebaseCommand rebase() {
        return target != null ? target.rebase() : super.rebase();
    }

    public RmCommand rm() {
        return target != null ? target.rm() : super.rm();
    }

    public CheckoutCommand checkout() {
        return target != null ? target.checkout() : super.checkout();
    }

    public ResetCommand reset() {
        return target != null ? target.reset() : super.reset();
    }

    public StatusCommand status() {
        return target != null ? target.status() : super.status();
    }

    public ArchiveCommand archive() {
        return target != null ? target.archive() : super.archive();
    }

    public AddNoteCommand notesAdd() {
        return target != null ? target.notesAdd() : super.notesAdd();
    }

    public RemoveNoteCommand notesRemove() {
        return target != null ? target.notesRemove() : super.notesRemove();
    }

    public ListNotesCommand notesList() {
        return target != null ? target.notesList() : super.notesList();
    }

    public ShowNoteCommand notesShow() {
        return target != null ? target.notesShow() : super.notesShow();
    }

    public LsRemoteCommand lsRemote() {
        return target != null ? target.lsRemote() : super.lsRemote();
    }

    public CleanCommand clean() {
        return target != null ? target.clean() : super.clean();
    }

    public BlameCommand blame() {
        return target != null ? target.blame() : super.blame();
    }

    public ReflogCommand reflog() {
        return target != null ? target.reflog() : super.reflog();
    }

    public DiffCommand diff() {
        return target != null ? target.diff() : super.diff();
    }

    public DeleteTagCommand tagDelete() {
        return target != null ? target.tagDelete() : super.tagDelete();
    }

    public SubmoduleAddCommand submoduleAdd() {
        return target != null ? target.submoduleAdd() : super.submoduleAdd();
    }

    public SubmoduleInitCommand submoduleInit() {
        return target != null ? target.submoduleInit() : super.submoduleInit();
    }

    public SubmoduleDeinitCommand submoduleDeinit() {
        return target != null ? target.submoduleDeinit() : super.submoduleDeinit();
    }

    public SubmoduleStatusCommand submoduleStatus() {
        return target != null ? target.submoduleStatus() : super.submoduleStatus();
    }

    public SubmoduleSyncCommand submoduleSync() {
        return target != null ? target.submoduleSync() : super.submoduleSync();
    }

    public SubmoduleUpdateCommand submoduleUpdate() {
        return target != null ? target.submoduleUpdate() : super.submoduleUpdate();
    }

    public StashListCommand stashList() {
        return target != null ? target.stashList() : super.stashList();
    }

    public StashCreateCommand stashCreate() {
        return target != null ? target.stashCreate() : super.stashCreate();
    }

    public StashApplyCommand stashApply() {
        return target != null ? target.stashApply() : super.stashApply();
    }

    public StashDropCommand stashDrop() {
        return target != null ? target.stashDrop() : super.stashDrop();
    }

    public ApplyCommand apply() {
        return target != null ? target.apply() : super.apply();
    }

    public GarbageCollectCommand gc() {
        return target != null ? target.gc() : super.gc();
    }

    public NameRevCommand nameRev() {
        return target != null ? target.nameRev() : super.nameRev();
    }

    public DescribeCommand describe() {
        return target != null ? target.describe() : super.describe();
    }

    public RemoteListCommand remoteList() {
        return target != null ? target.remoteList() : super.remoteList();
    }

    public RemoteAddCommand remoteAdd() {
        return target != null ? target.remoteAdd() : super.remoteAdd();
    }

    public RemoteRemoveCommand remoteRemove() {
        return target != null ? target.remoteRemove() : super.remoteRemove();
    }

    public RemoteSetUrlCommand remoteSetUrl() {
        return target != null ? target.remoteSetUrl() : super.remoteSetUrl();
    }

    public Repository getRepository() {
        return target != null ? target.getRepository() : super.getRepository();
    }

    public String toString() {
        return target != null ? target.toString() : super.toString();
    }
}
