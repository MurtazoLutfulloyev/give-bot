package my_bot.givebot.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "users")
@Component
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Long chatId;

    @Column(name = "full_name")
    private  String fullName;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "phone")
    private String phone;
    @Transient
    private int round;

    @ManyToMany
    private List<Group> group;






}
