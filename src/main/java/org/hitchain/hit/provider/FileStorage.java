/*******************************************************************************
 * Copyright (c) 2018-11-22 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.hitchain.hit.provider;

import gitbucket.core.util.Tuple;
import org.eclipse.jgit.internal.storage.file.PackIndex;
import org.hitchain.hit.api.HashedFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * FileStorage
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-22
 * auto generate by qdp.
 */
public class FileStorage {
    private String basePath;

    public static void main(String[] args) {
        try {
            //FileStorage fs = create("/Users/zhaochen/.gitbucket/repositories");
            //HashedFile tree = fs.listFileTree("root/test.git");
            //System.out.println(new ObjectMapper().writeValueAsString(tree));

            HashedFile hf = new HashedFile.FileWrapper("/Users/zhaochen/.gitbucket/repositories/root/test.git/objects/pack/pack-436b0d408586a3b57f96ee51b9b469a3e7e342ae.idx", new HashedFile.InputStreamCallback() {
                public InputStream call(HashedFile hashedFile) throws IOException {
                    return new FileInputStream(new File(hashedFile.getName()));
                }
            });
            InputStream is = hf.getInputStream();
            PackIndex read = PackIndex.read(is);
            is.close();
            Iterator<PackIndex.MutableEntry> iterator = read.iterator();
            while (iterator.hasNext()) {
                PackIndex.MutableEntry next = iterator.next();
                System.out.println(next.toObjectId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FileStorage create(String basePath) {
        FileStorage fs = new FileStorage();
        fs.basePath = basePath;
        return fs;
    }

    public HashedFile listFileTree(String relativePath) throws IOException {
        HashedFile.DirWrapper root = new HashedFile.DirWrapper(basePath);
        HashedFile.InputStreamCallback callback = new HashedFile.InputStreamCallback() {
            public InputStream call(HashedFile hashedFile) throws IOException {
                return new FileInputStream(new File(hashedFile.getName()));
            }
        };
        File rootFile = new File(basePath, relativePath);
        if (rootFile.isFile()) {
            root.getChildren().add(new HashedFile.FileWrapper(rootFile.getAbsolutePath(), callback));
            return root;
        }
        List<Tuple.Two<HashedFile, File>> folder = new LinkedList<>();
        folder.add(new Tuple.Two<>(root, rootFile));
        Tuple.Two<HashedFile, File> tmp = null;
        while (folder.size() > 0 && (tmp = folder.remove(0)) != null) {
            HashedFile parent = tmp.getFirst();
            File file = tmp.getSecond();
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isDirectory()) {
                    HashedFile.DirWrapper dir = new HashedFile.DirWrapper(f.getAbsolutePath());
                    parent.getChildren().add(dir);
                    folder.add(new Tuple.Two<>(dir, f));
                    continue;
                }
                parent.getChildren().add(new HashedFile.FileWrapper(f.getAbsolutePath(), callback));
            }
        }
        return root;
    }
}
