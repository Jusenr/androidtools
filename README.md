# AndroidTools #
Android Some of the tools used in development.

---
[![Bintray](https://img.shields.io/badge/bintray-v1.3.3-brightgreen.svg)](https://bintray.com/jusenr/maven/androidtools)
[![Download](https://api.bintray.com/packages/jusenr/maven/androidtools/images/download.svg)](https://bintray.com/jusenr/maven/androidtools/_latestVersion)
[![Jitpack](https://jitpack.io/v/Jusenr/androidtools.svg)](https://jitpack.io/#Jusenr/androidtools)
[![License](http://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square)](http://www.apache.org/licenses/LICENSE-2.0)

<a href='https://bintray.com/jusenr/maven/androidtools?source=watch' alt='Get automatic notifications about new "androidtools" versions'><img src='https://www.bintray.com/docs/images/bintray_badge_color.png'></a>

#### To get a Git project into your build: ####

---

#####  #####

>Step 1. Dependent Manner

 The First： Add the JitPack repository to your build file

(1) Add it in your root build.gradle at the end of repositories:

    allprojects {
            repositories {
                ...
                maven { url 'https://jitpack.io' }
            }
    }

 (2) Add the dependency

        dependencies {
         ...
        compile 'com.github.Jusenr:androidtools:1.3.3'
        }

---

  The Second: Add the dependency

          dependencies {
           ...
          compile 'com.jusenr.android.library:androidtools:1.3.3'
          }

-----

>Step 3. Add required permissions

    <manifest
        ...
        <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
        <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
        <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
        <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>

         <application
            ...
               <!--Log view page-->
               <activity
                    android:name="com.jusenr.toolslibrary.log.PTLogActivity"
                    android:screenOrientation="portrait"/>

         </application>

    </manifest>

---

>Step 4. Initialization configuration

    App extends Application{

        @Override
        public void onCreate() {
           super.onCreate();

           //AndroidTools initialise.
           AndroidTools.init(getApplicationContext(), "LogTag");
       }
    }

---

#### It was smashing! Setup Complete!!! ####

---

#### Express one's thanks ####

Thanks to Guchenkai, Trinea, Riven_chris, Zengshengwen, Leo, Sunnybear, Liuwan, Wangxiaopeng, Orhanobut provides the resources.

---

#### Possible solutions to the problem ####

   1. Support packet conflict problem

   Add Build.gradle to the project: The version is the current version of your project.

        subprojects {
            configurations.all {
                resolutionStrategy {
                    force 'com.android.support:support-xx:xx.x.x'
                }
            }
        }

---

#### License ####

[Apache License Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)
