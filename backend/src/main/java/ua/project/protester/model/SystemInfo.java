package ua.project.protester.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
@Getter
public class SystemInfo {

    private final String projectName;
    private final List<String> projectMembers;
    private final List<String> projectMentors;
    private final String projectLink;
    private final String gitLink;
    private final String requestTimestamp;

    public static SystemInfo get() {
        return SystemInfo.builder()
                .projectName("ProTester")
                .projectMembers(List.of(
                        "Ehor Shulga",
                        "Herman Smolar",
                        "Julia Borovets",
                        "Vadim Dudka",
                        "Vladislav Tochanenko",
                        "Volodymyr Zhuk"))
                .projectMentors(List.of(
                        "Serhii Yablonovskyi",
                        "Yevhenii Deineka"))
                .projectLink("https://pro-tester.herokuapp.com/")
                .gitLink("https://github.com/tochanenko/ProTester")
                .requestTimestamp(OffsetDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .build();
    }
}
