whoami;
pwd;
mkdir self-todos-tmp-%s && cd self-todos-tmp-%s;
git clone git@%s.com:%s;
cd %s && java -jar /usr/local/bin/todo-finder-cli.jar;
