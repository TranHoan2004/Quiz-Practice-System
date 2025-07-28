/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.UUID;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SettingQuestion {
    private UUID  settingId; // UUID sẽ được chuyển đổi thành String khi sử dụng
    private String questionId; // UUID sẽ được chuyển đổi thành String khi sử dụng
}
