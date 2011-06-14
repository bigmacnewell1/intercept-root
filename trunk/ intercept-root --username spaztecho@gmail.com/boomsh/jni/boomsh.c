#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>

static char *rooted = "/data/data/com.intercept.root/files/rooted";

int main(int argc, char **argv, char **env)
{
	FILE *File=fopen(rooted,"w+");
	execlp("/data/data/com.intercept.root/files/install.sh", "/data/data/com.intercept.root/files/install.sh", (char *)NULL);
	return 0;
}
