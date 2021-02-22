#!/bin/bash
set -ex
wget https://www.dropbox.com/s/l84qhhfa4q5d59c/google-chrome-stable_current_amd64.deb?dl=1
sudo apt install ./google-chrome-stable_current_amd64.deb
wget https://www.dropbox.com/s/yd8w6q8jccomiev/chromedriver_linux64.zip?dl=1
unzip chromedriver_linux64.zip
chmod 755 chromedriver
