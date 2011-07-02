#
# Copyright (C) 2008 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# This makefile supplies the rules for building a library of JNI code for
# use by our example of how to bundle a shared library with an APK.

# GingerBreak
LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

# This is the target being built.
LOCAL_MODULE:= libGingerBreak

# All of the source files that we will compile.
LOCAL_SRC_FILES:= \
  GingerBreak.c

LOCAL_LDLIBS := -ldl -llog

include $(BUILD_SHARED_LIBRARY)

# boomsh
include $(CLEAR_VARS)

# This is the target being built.
LOCAL_MODULE:= boomsh

# All of the source files that we will compile.
LOCAL_SRC_FILES:= \
  boomsh.c

LOCAL_LDLIBS := -ldl -llog

include $(BUILD_EXECUTABLE)

# RunCmd
include $(CLEAR_VARS)

# This is the target being built.
LOCAL_MODULE:= libRunCmd

# All of the source files that we will compile.
LOCAL_SRC_FILES:= \
  RunCmd.c

LOCAL_LDLIBS := -ldl -llog

include $(BUILD_SHARED_LIBRARY)

# su
include $(CLEAR_VARS)

LOCAL_SRC_FILES:= su.c

LOCAL_MODULE:= su

LOCAL_FORCE_STATIC_EXECUTABLE := true

LOCAL_STATIC_LIBRARIES := libc

include $(BUILD_EXECUTABLE)

# Zysploit
include $(CLEAR_VARS)

# This is the target being built.
LOCAL_MODULE:= Zysploit

# All of the source files that we will compile.
LOCAL_SRC_FILES:= \
	bionic_clone.c \
	clone.S \
	Zysploit.c

LOCAL_LDLIBS := -ldl -llog

include $(BUILD_SHARED_LIBRARY)