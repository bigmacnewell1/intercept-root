#include "jni.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <errno.h>
#include <unistd.h>

jint JNI_OnLoad(JavaVM* vm, void* reserved) {
	jint result = JNI_VERSION_1_4;
	return result;
}

JNIEXPORT void JNICALL 
Java_com_intercept_root_RunCmd_Exec(JNIEnv *env, jobject obj, jstring javaCmd)   
{
	const char *cmd = (*env)->GetStringUTFChars(env, javaCmd, 0);
	runcmd(cmd);
	(*env)->ReleaseStringUTFChars(env, javaCmd, cmd);
}

int runcmd(char *cmd)
{
  pid_t child_pid, tpid;
  int child_status;

  child_pid = fork();
  if(child_pid == 0) {
    /* This is done by the child process. */
    if (execl("/system/xbin/su", "su", cmd, (char *)0) < 0) {
		fprintf(stderr, "su: exec failed for %s Error:%s\n", cmd,
			strerror(errno));
		return -errno;
    }
  }
  else {
     /* This is run by the parent.  Wait for the child
        to terminate. */
     do {
       tpid = wait(&child_status);
     } while(tpid != child_pid);
     return child_status;
  }
  return 1;
}