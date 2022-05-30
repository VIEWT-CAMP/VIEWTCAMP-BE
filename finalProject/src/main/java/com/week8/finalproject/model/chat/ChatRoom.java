
package com.week8.finalproject.model.chat;

import com.week8.finalproject.model.chat.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long Id;

    @Column
    private String roomId;

    @Column
    private String name;

    @OneToMany
    private List<ChatMessage> chatMessage;

}
