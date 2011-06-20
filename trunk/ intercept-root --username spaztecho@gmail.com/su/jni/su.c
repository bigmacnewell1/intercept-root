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
    int uid, gid, cmdStart;
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
	
	/* User specified command for exec. */
	if(argc > cmdStart)
	{
		if(cmdStart == 1)
		{
			char *exec_args[argc-1];
			while (cmdStart < argc)
			{
				exec_args[cmdStart - 1] = argv[cmdStart];
				cmdStart++;
			}
			char cmd[250];
			strcpy (cmd, exec_args[0]);
			int i = 1;
			while (i<argc-1) {
				strcat (cmd," ");
				strcat (cmd, exec_args[i]);
				i++;
			}
			system(cmd);
			exit(0);
		}
		else
		{
			char *exec_args[argc-2];
			while (cmdStart < argc)
			{
				exec_args[cmdStart-2] = argv[cmdStart];
				cmdStart++;
			}
			char cmd[250];
			strcpy (cmd, exec_args[0]);
			int i = 1;
			while (i<argc-2) {
				strcat (cmd," ");
				strcat (cmd, exec_args[i]);
				i++;
			}
			system(cmd);
			exit(0);
		}
	}

    /* Default exec shell. */
    execlp("/system/bin/sh", "sh", NULL);

    fprintf(stderr, "su: exec failed\n");
    return 1;
}
