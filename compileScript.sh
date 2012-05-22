#!/bin/bash

x=$(echo $1 | cut -d '.' -f 1);
echo $x
cd src; javac Scanner.java; cd ..
nvcc -ptx -Xopencc="LIST:source=on" $1
cd src; java Scanner "../"$x".cu" "../"$x".ptx"
