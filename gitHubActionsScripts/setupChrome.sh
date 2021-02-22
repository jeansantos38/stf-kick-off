#!/bin/bash
set -ex

wget -O chromedriver_linux64.zip https://www.dropbox.com/s/yd8w6q8jccomiev/chromedriver_linux64.zip?dl=1
ls -la
pwd
unzip chromedriver_linux64.zip
chmod 755 chromedriver


echo "starting step1!"
wget -O google-chrome-stable_current_amd64.deb https://www.dropbox.com/s/l84qhhfa4q5d59c/google-chrome-stable_current_amd64.deb?dl=1
ls
sudo apt install ./google-chrome-stable_current_amd64.deb
echo "step1 completed!"
echo "starting step2!"

echo "step2 completed!"

