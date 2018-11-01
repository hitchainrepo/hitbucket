package gitbucket.core.util

import java.io.File

import gitbucket.core.service.SystemSettingsService
import gitbucket.core.util.SyntaxSugars.defining
import org.apache.commons.io.FileUtils
import org.apache.http.HttpHost
import org.apache.http.auth.{AuthScope, UsernamePasswordCredentials}
import org.apache.http.impl.client.{BasicCredentialsProvider, CloseableHttpClient, HttpClientBuilder}

object WaitToReplaceUtil {

  /**
   * gitbucket.core.util.FileUtil#withTmpDir(java.io.File, scala.Function1)
   *
   * @param dir
   * @param action
   * @tparam A
   * @return
   */
  def withTmpDir[A](dir: File)(action: File => A): A = {
    if (dir.exists()) {
      FileUtils.deleteDirectory(dir)
    }
    try {
      action(dir)
    } finally {
      FileUtils.deleteDirectory(dir)
    }
  }

  /**
   * gitbucket.core.util.HttpClientUtil#withHttpClient(scala.Option, scala.Function1)
   *
   * @param proxy
   * @param f
   * @tparam T
   * @return
   */
  def withHttpClient[T](proxy: Option[SystemSettingsService.Proxy])(f: CloseableHttpClient => T): T = {
    val builder = HttpClientBuilder.create.useSystemProperties

    proxy.foreach { proxy =>
      builder.setProxy(new HttpHost(proxy.host, proxy.port))

      for (user <- proxy.user; password <- proxy.password) {
        val credential = new BasicCredentialsProvider()
        credential.setCredentials(
          new AuthScope(proxy.host, proxy.port),
          new UsernamePasswordCredentials(user, password)
        )
        builder.setDefaultCredentialsProvider(credential)
      }
    }

    val httpClient = builder.build()

    try {
      f(httpClient)
    } finally {
      httpClient.close()
    }
  }

  /**
   * gitbucket.core.util.LockUtil#lock(java.lang.String, scala.Function0)
   * Synchronizes a given function which modifies the working copy of the wiki repository.
   */
  def lock[T](key: String)(f: => T): T = defining(LockUtilHelper.getLockObject(key)) { lock =>
    try {
      lock.lock()
      f
    } finally {
      lock.unlock()
    }
  }
}
