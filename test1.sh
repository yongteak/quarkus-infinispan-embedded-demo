# tcp port test
nc -v ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com 7800


./start.sh --node-prefix=1 --initial_hosts="192.168.12.113[7800]"

# native
./nstart.sh --node-prefix=1 --initial_hosts="192.168.12.113[7800],192.168.12.113[7801],192.168.12.113[7803],192.168.12.113[7804]"

./start.sh --node-prefix=4 --bind-address="172.31.59.219" --gossip-hosts="ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[12001]" --initial_hosts="ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7800],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7801],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7803],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7804]"
./start.sh --node-prefix=5 --bind-address="172.31.59.219" --gossip-hosts="ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[12001]" --initial_hosts="ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7800],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7801],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7803],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7804]"
./start.sh --node-prefix=6 --bind-address="172.31.59.219" --gossip-hosts="ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[12001]" --initial_hosts="ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7800],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7801],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7803],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7804]"


./start.sh --node-prefix=1  --bind-address="192.168.12.113" --gossip-hosts="ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[12001]" --initial_hosts="ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7800],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7801],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7803],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7804]"
./start.sh --node-prefix=2  --bind-address="192.168.12.113" --gossip-hosts="ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[12001]" --initial_hosts="ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7800],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7801],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7803],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7804]"
./start.sh --node-prefix=3  --bind-address="192.168.12.113" --gossip-hosts="ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[12001]" --initial_hosts="ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7800],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7801],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7803],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7804]"
./start.sh --node-prefix=4  --bind-address="192.168.12.113" --gossip-hosts="ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[12001]" --initial_hosts="ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7800],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7801],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7803],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7804]"


./start.sh --node-prefix=2 --bind-address="172.31.59.219" --gossip-hosts="ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[12001]"
./start.sh --node-prefix=2 --bind-address="172.31.59.219" --initial_hosts="ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7800],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7801],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7803],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7804]"

./nstart.sh --node-prefix=2 --initial_hosts="ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7800],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7801],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7803],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7804]"

./start.sh --node-prefix=1 --initial_hosts="ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7800],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7801],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7803],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7804]"

./start.sh --node-prefix=3 --initial_hosts="ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7800],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7801],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7803],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7804]"
./start.sh --node-prefix=4 --initial_hosts="ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7800],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7801],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7803],ec2-3-38-112-179.ap-northeast-2.compute.amazonaws.com[7804]"
./nstart.sh --node-prefix=1 --initial_hosts="192.168.12.113[7800],192.168.12.113[7801],192.168.12.113[7803],192.168.12.113[7804]"
./nstart.sh --node-prefix=1 --initial_hosts="192.168.12.113[7800],192.168.12.113[7801],192.168.12.113[7803],192.168.12.113[7804]"
