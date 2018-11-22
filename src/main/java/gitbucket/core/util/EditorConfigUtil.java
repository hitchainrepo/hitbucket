/*******************************************************************************
 * Copyright (c) 2018-11-08 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package gitbucket.core.util;

import org.ec4j.core.*;
import org.ec4j.core.Resource.Resources.StringRandomReader;
import org.ec4j.core.model.Ec4jPath;
import org.ec4j.core.model.PropertyType;
import org.ec4j.core.model.Version;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 * EditorConfigUtil
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-08
 * auto generate by qdp.
 */
public class EditorConfigUtil {

    private static final int TabSizeDefault = 8;
    private static final String NewLineModeDefault = "auto";
    private static final boolean UseSoftTabsDefault = false;

    public static EditorConfigInfo getEditorConfigInfo(Git git, String rev, String path) {
        try {
            ResourcePropertiesService resourcePropertiesService = ResourcePropertiesService
                .builder()
                .configFileName(EditorConfigConstants.EDITORCONFIG)
                .rootDirectory(JGitResourcePath.rootDirectory(git, rev))
                .loader(EditorConfigLoader.of(Version.CURRENT))
                .keepUnset(true)
                .build();

            ResourceProperties props = resourcePropertiesService.queryProperties(new JGitResource(git, rev, path));
            Integer tabSize = props.getValue(PropertyType.tab_width, TabSizeDefault, false);
            PropertyType.EndOfLineValue endOfLineValue = props.getValue(PropertyType.end_of_line, null, false);
            String newLineMode = "auto";
            if (PropertyType.EndOfLineValue.cr.equals(endOfLineValue)) {
                newLineMode = "cr";
            } else if (PropertyType.EndOfLineValue.lf.equals(endOfLineValue)) {
                newLineMode = "ls";
            } else if (PropertyType.EndOfLineValue.crlf.equals(endOfLineValue)) {
                newLineMode = "crlf";
            }

            PropertyType.IndentStyleValue value = props.getValue(PropertyType.indent_style, null, false);
            boolean useSoftTabs = PropertyType.IndentStyleValue.space.equals(value);
            return new EditorConfigInfo(tabSize, newLineMode, useSoftTabs);
        } catch (Exception e) {
            return new EditorConfigInfo(TabSizeDefault, NewLineModeDefault, UseSoftTabsDefault);
        }
    }

    private static class JGitResource implements Resource {
        private Repository repo;
        private String revStr;
        private Ec4jPath path;

        public JGitResource(Repository repo, String revStr, Ec4jPath path) {
            this.repo = repo;
            this.revStr = revStr;
            this.path = path;
        }

        public JGitResource(Git git, String revStr, String path) {
            this(git.getRepository(), revStr, Ec4jPath.Ec4jPaths.of(path.startsWith("/") ? path : ("/" + path)));
        }

        public JGitResource(Repository repo, String revStr, String path) {
            this(repo, revStr, Ec4jPath.Ec4jPaths.of(path.startsWith("/") ? path : ("/" + path)));
        }

        private String removeInitialSlash(Ec4jPath path) {
            return Ec4jPath.Ec4jPaths.root().relativize(path).toString();
        }

        private RevTree getRevTree() {
            try {
                ObjectReader reader = repo.newObjectReader();
                RevWalk revWalk = new RevWalk(reader);
                ObjectId id = repo.resolve(revStr);
                RevCommit commit = revWalk.parseCommit(id);
                return commit.getTree();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public boolean exists() {
            ObjectReader reader = repo.newObjectReader();
            try {
                TreeWalk treeWalk = TreeWalk.forPath(reader, removeInitialSlash(path), getRevTree());
                return treeWalk != null;
            } catch (Exception e) {
                return false;
            }

        }

        public Ec4jPath getPath() {
            return path;
        }

        public ResourcePath getParent() {
            Ec4jPath path = this.path.getParentPath();
            if (path == null) {
                return null;
            }
            return new JGitResourcePath(repo, revStr, path);
        }

        public Resource.RandomReader openRandomReader() throws IOException {
            return StringRandomReader.ofReader(openReader());
        }

        public Reader openReader() throws IOException {
            ObjectReader reader = repo.newObjectReader();
            TreeWalk treeWalk = TreeWalk.forPath(reader, removeInitialSlash(path), getRevTree());
            return new InputStreamReader(reader.open(treeWalk.getObjectId(0)).openStream(), StandardCharsets.UTF_8);
        }
    }

    private static class JGitResourcePath implements ResourcePath {
        private Repository repo;
        private String revStr;
        private Ec4jPath path;

        public JGitResourcePath(Repository repo, String revStr, Ec4jPath path) {
            this.repo = repo;
            this.revStr = revStr;
            this.path = path;
        }

        public static JGitResourcePath rootDirectory(Git git, String revStr) {
            return new JGitResourcePath(git.getRepository(), revStr, Ec4jPath.Ec4jPaths.of("/"));
        }

        public ResourcePath getParent() {
            Ec4jPath path = this.path.getParentPath();
            return path == null ? null : new JGitResourcePath(repo, revStr, path);
        }

        public Ec4jPath getPath() {
            return path;
        }

        public boolean hasParent() {
            return path.getParentPath() != null;
        }

        public Resource relativize(Resource resource) {
            if (resource instanceof JGitResource) {
                return new JGitResource(repo, revStr, path.relativize(resource.getPath()).toString());
            }
            return resource;
        }

        public Resource resolve(String name) {
            if (path != null) {
                return new JGitResource(repo, revStr, path.resolve(name));
            } else {
                return new JGitResource(repo, revStr, name);
            }
        }
    }

    public static class EditorConfigInfo {
        public int tabSize;
        public String newLineMode;
        public boolean useSoftTabs;

        public EditorConfigInfo(int tabSize, String newLineMode, boolean useSoftTabs) {
            this.tabSize = tabSize;
            this.newLineMode = newLineMode;
            this.useSoftTabs = useSoftTabs;
        }

        public int getTabSize() {
            return tabSize;
        }

        public void setTabSize(int tabSize) {
            this.tabSize = tabSize;
        }

        public String getNewLineMode() {
            return newLineMode;
        }

        public void setNewLineMode(String newLineMode) {
            this.newLineMode = newLineMode;
        }

        public boolean isUseSoftTabs() {
            return useSoftTabs;
        }

        public void setUseSoftTabs(boolean useSoftTabs) {
            this.useSoftTabs = useSoftTabs;
        }
    }
}
