/*
**
** Copyright 2008, The Android Open Source Project
**
** Licensed under the Apache License, Version 2.0 (the "License"); 
** you may not use this file except in compliance with the License. 
** You may obtain a copy of the License at 
**
**     http://www.apache.org/licenses/LICENSE-2.0 
**
** Unless required by applicable law or agreed to in writing, software 
** distributed under the License is distributed on an "AS IS" BASIS, 
** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
** See the License for the specific language governing permissions and 
** limitations under the License.
*/

#define LOG_TAG "su"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <dirent.h>
#include <errno.h>

#include <unistd.h>
#include <time.h>

#include <pwd.h>

#include "android_filesystem_config.h"

/*
 * SU can be given a specific command to exec. UID _must_ be
 * specified for this (ie argc => 3). Not any More Spaztecho :)
 *
 * Usage:
 * su 1000
 * su 1000 ls -l
 * Now this works too!
 * su ls -l
 */
 
char *mkconcat(char **, int);
 
int main(int argc, char **argv)
{
    struct passwd *pw;
    int uid, gid, cmdStart, index;
	char *cmd = NULL;

	// Get user id
    if(argc < 2) {
        uid = gid = 0;
		cmdStart = 1;
    } else {
		if ((strcmp (argv[1],"root") == 0) || (strcmp (argv[1],"0") == 0))
		{
			uid = gid = 0;
			cmdStart = 2;
		}
		else if (atoi(argv[1]) != 0)
		{
			uid = gid = atoi(argv[1]);
			cmdStart = 2;
		}
		else if ((pw = getpwnam(argv[1])) != 0)
		{
			uid = pw->pw_uid;
            gid = pw->pw_gid;
			cmdStart = 2;
		}
        else {
			uid = gid = 0;
			cmdStart = 1;
		}
    }

    /* Until we have something better, only root and the shell can use su. 
    myuid = getuid();
    if (myuid != AID_ROOT && myuid != AID_SHELL) {
        fprintf(stderr,"su: uid %d not allowed to su\n", myuid);
        return 1;
    }
	now everyone can su! spaztecho
	*/
    
    if(setgid(gid) || setuid(uid)) {
        fprintf(stderr,"su: permission denied\n");
        return 1;
    }
	
	/* Some apps use su -c to exec code, I don't know why they shouldn't. 
	 *  Lets fix their command for them , and since we exec though sh lets remove that too
	 */ 
	while(argc > cmdStart)
	{
		if ((strcmp (argv[cmdStart],"sh") == 0) || (strcmp (argv[cmdStart],"-c") == 0))
		{
			cmdStart++;
		}
		else
		{
			break;
		}
	}

	
	/* User specified command for exec. */
	if(argc > cmdStart)
	{
		index = cmdStart;
		char *exec_args[argc-cmdStart];
		while (index < argc)
		{
			exec_args[index - cmdStart] = argv[index];
			index++;
		}
		char cmd[250];
		strcpy (cmd, exec_args[0]);
		index = 1;
		while (index < argc-cmdStart) {
			strcat (cmd," ");
			strcat (cmd, exec_args[index]);
			index++;
		}
		if (execl("/system/bin/sh", "sh", "-c", cmd, (char *)0) < 0) {
            fprintf(stderr, "su: exec failed for %s Error:%s\n", argv[cmdStart],
                    strerror(errno));
            return -errno;
        }
	}

    /* Default exec shell. */
    execlp("/system/bin/sh", "sh", NULL);

    fprintf(stderr, "su: exec failed\n");
    return 1;
}
