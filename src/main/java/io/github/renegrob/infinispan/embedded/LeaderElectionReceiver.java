package io.github.renegrob.infinispan.embedded;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.Receiver;
import org.jgroups.View;
import org.jgroups.util.Util;

import java.util.List;

// Receiver 인터페이스를 구현하는 클래스로 리더 선출 로직 포함
public class LeaderElectionReceiver implements Receiver {
    private JChannel channel;

    public LeaderElectionReceiver(JChannel channel) {
        this.channel = channel;
    }

    @Override
    public void viewAccepted(View new_view) {
        List<Address> members = new_view.getMembers();
        int leaderCount = (int) Math.ceil(members.size() * 0.05); // 총 노드의 5% 계산
        leaderCount = Math.max(leaderCount, 1); // 최소 한 개의 리더는 선출

        List<Address> leaders = members.subList(0, leaderCount);
        if (leaders.contains(channel.address())) {
            System.out.println("I am a leader.");
        } else {
            System.out.println("I am not a leader.");
        }
    }

    @Override
    public void receive(Message msg) {
        // 메시지 수신 관련 로직이 필요한 경우 여기에 추가
    }
}