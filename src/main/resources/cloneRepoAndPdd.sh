whoami
pwd
mkdir self-todos-tmp-%s && cd self-todos-tmp-%s
git clone git@%s:%s repo && cd repo
java -jar /usr/local/bin/todo-finder-cli.jar
