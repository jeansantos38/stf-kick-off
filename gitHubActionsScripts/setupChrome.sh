#!/bin/bash
set -ex
echo "Downloading chromedriver for v.88!"
wget -O chromedriver_linux64.zip https://www.dropbox.com/s/yd8w6q8jccomiev/chromedriver_linux64.zip?dl=1
echo "Unziping chromedriver!"
unzip chromedriver_linux64.zip
echo "Changing chromedriver permissions!"
chmod 755 chromedriver
echo "Chromedriver setup is complete!"

echo "Downloading chrome browser v.88!"
wget -O google-chrome-stable_current_amd64.deb https://www.dropbox.com/s/l84qhhfa4q5d59c/google-chrome-stable_current_amd64.deb?dl=1
echo "Installing chrome browser v.88!"
sudo apt install --allow-downgrades ./google-chrome-stable_current_amd64.deb
echo "Chrome was installed!"