#include <stdio.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>

static char *rooted = "/data/data/com.intercept.root/files/rooted";
static char *temp = "/data/data/com.intercept.root/files/tempRoot";

int main(int argc, char **argv, char **env)
{
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