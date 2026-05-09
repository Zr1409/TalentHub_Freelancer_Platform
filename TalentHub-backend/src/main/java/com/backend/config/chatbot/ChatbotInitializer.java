package com.backend.config.chatbot;

import jakarta.annotation.PostConstruct;
import com.backend.entity.child.chatbot.ChatIntent;
import com.backend.entity.child.chatbot.ChatResponse;
import com.backend.entity.child.chatbot.ChatTrainingPhrase;
import com.backend.repository.ChatIntentRepository;
import com.backend.repository.ChatResponseRepository;
import com.backend.repository.ChatTrainingPhraseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Configuration
public class ChatbotInitializer {

    @Autowired
    private ChatIntentRepository chatIntentRepository;

    @Autowired
    private ChatTrainingPhraseRepository chatTrainingPhraseRepository;

    @Autowired
    private ChatResponseRepository chatResponseRepository;

    @PostConstruct
    @Transactional
    public void initializeChatbot() {
        // Check if we already have some intents
        if (chatIntentRepository.count() > 0) {
            return; // Already initialized
        }

        // Initialize basic intents
        createGreetingIntent();
        createJobBySkillsIntent();
        createTrendingSkillsIntent();
        createPopularCategoriesIntent();
        createJobCountBySkillIntent();
        createJobCountByCategoryIntent();
        createHelpIntent();
    }

    private void createGreetingIntent() {
        ChatIntent intent = new ChatIntent();
        intent.setIntentName("greeting");
        intent.setDescription("Xử lý chào hỏi từ người dùng");
        intent = chatIntentRepository.save(intent);

        // Training phrases
        List<String> trainingPhrases = Arrays.asList(
                "Xin chào",
                "Chào bạn",
                "Hello",
                "Hi",
                "Chào buổi sáng",
                "Chào",
                "Bạn khỏe không",
                "Trợ giúp"
        );

        for (String phrase : trainingPhrases) {
            ChatTrainingPhrase trainingPhrase = new ChatTrainingPhrase();
            trainingPhrase.setPhraseText(phrase);
            trainingPhrase.setIsProcessed(true);
            trainingPhrase.setIntent(intent);
            chatTrainingPhraseRepository.save(trainingPhrase);
        }

        // Responses
        List<String> responses = Arrays.asList(
                "Xin chào! Tôi là trợ lý ảo của TalentHub. Tôi có thể giúp bạn tìm kiếm công việc phù hợp với kỹ năng, cung cấp thông tin về các kỹ năng đang hot, hoặc số lượng công việc theo danh mục. Bạn cần hỗ trợ gì?",
                "Chào bạn! Tôi có thể giúp bạn tìm công việc theo kỹ năng, cho bạn biết những kỹ năng đang được săn đón hoặc cung cấp thông tin về số lượng công việc hiện có. Bạn muốn biết thêm về điều gì?"
        );

        int order = 0;
        for (String response : responses) {
            ChatResponse chatResponse = new ChatResponse();
            chatResponse.setResponseText(response);
            chatResponse.setDisplayOrder(order++);
            chatResponse.setIntent(intent);
            chatResponseRepository.save(chatResponse);
        }
    }

    private void createJobBySkillsIntent() {
        ChatIntent intent = new ChatIntent();
        intent.setIntentName("job_by_skills");
        intent.setDescription("Tìm công việc phù hợp với kỹ năng");
        intent = chatIntentRepository.save(intent);

        // Training phrases (giữ nguyên)
        List<String> trainingPhrases = Arrays.asList(
                "Tôi có những kỹ năng {skills} thì có công việc nào phù hợp không?",
                "Với kỹ năng {skills} thì tôi có thể làm công việc gì?",
                "Có công việc nào cho người biết {skills} không?",
                "Tôi biết {skills}, tìm việc làm gì được?",
                "Kỹ năng {skills} phù hợp với công việc nào?",
                "Những công việc nào cần kỹ năng {skills}?",
                "Tôi giỏi {skills}, tìm được việc gì?",
                "Có công việc nào cho skill {skills} hay không?",
                "Công việc cho người có kỹ năng {skills}",
                "Ai cần người biết {skills}?",
                "Đang có công việc nào yêu cầu {skills}?"
        );

        // Thêm các phrases vào DB (giữ nguyên)
        for (String phrase : trainingPhrases) {
            ChatTrainingPhrase trainingPhrase = new ChatTrainingPhrase();
            trainingPhrase.setPhraseText(phrase);
            trainingPhrase.setIsProcessed(true);
            trainingPhrase.setIntent(intent);
            chatTrainingPhraseRepository.save(trainingPhrase);
        }

        ChatResponse response = new ChatResponse();
        response.setResponseText("Với kỹ năng {{skills}}, hiện có {{job_count}} công việc phù hợp. Các công việc phổ biến nhất là: {{title SEPARATOR '}}. Bạn có thể vào mục Quản lý công việc => Công việc để có thể tìm kiếm từ khóa và xem chi tiết hơn.\n" +
                "\n");
        response.setDisplayOrder(0);
        response.setIntent(intent);
        response.setRequiresDbQuery(true);
        response.setQueryTemplate(
                "SELECT '{{skills}}' as skills, " +
                        "COUNT(DISTINCT j.id) as job_count, " +
                        "(SELECT GROUP_CONCAT(DISTINCT j.title SEPARATOR ', ') " +
                        " FROM job j " +
                        " JOIN job_skill js ON j.id = js.job_id " +
                        " JOIN skill s ON js.skill_id = s.id " +
                        " WHERE " +
                        " LOWER(s.skill_name) LIKE LOWER('%{{skills}}%') " +
                        " LIMIT 3) as popular_jobs " +
                        "FROM job j " +
                        "JOIN job_skill js ON j.id = js.job_id " +
                        "JOIN skill s ON js.skill_id = s.id " +
                        "WHERE " +
                        "LOWER(s.skill_name) LIKE LOWER('%{{skills}}%')"
        );
        chatResponseRepository.save(response);

        // Fallback response (giữ nguyên)
        ChatResponse fallbackResponse = new ChatResponse();
        fallbackResponse.setResponseText("Với kỹ năng {{skills}}, hiện tại tôi không tìm thấy công việc cụ thể trong cơ sở dữ liệu. Tuy nhiên, đây là những kỹ năng được nhiều nhà tuyển dụng tìm kiếm. Bạn có thể thử tìm kiếm với từ khóa tương tự hoặc kiểm tra danh sách việc làm mới nhất trên trang chủ của chúng tôi.");
        fallbackResponse.setDisplayOrder(1);
        fallbackResponse.setIntent(intent);
        fallbackResponse.setRequiresDbQuery(false);
        chatResponseRepository.save(fallbackResponse);
    }
    private void createJobCountBySkillIntent() {
        ChatIntent intent = new ChatIntent();
        intent.setIntentName("job_count_by_skill");
        intent.setDescription("Đếm số lượng công việc theo kỹ năng");
        intent = chatIntentRepository.save(intent);

        // Training phrases
        List<String> trainingPhrases = Arrays.asList(
                "Hiện tại có bao nhiêu công việc với skill {skill}?",
                "Có bao nhiêu việc làm yêu cầu {skill}?",
                "Đếm số công việc cần kỹ năng {skill}",
                "Số lượng công việc cho người biết {skill}",
                "Mấy công việc cần {skill}?",
                "{skill} có bao nhiêu công việc?",
                "Đếm việc làm cho {skill}",
                "Tổng số việc làm về {skill}",
                "Có bn công việc về {skill}?",
                "Công việc với skill {skill}?",
                "Cho tôi danh sách công việc yêu cầu {skill}?",
                "Việc làm {skill} hiện có bao nhiêu?"
        );

        for (String phrase : trainingPhrases) {
            ChatTrainingPhrase trainingPhrase = new ChatTrainingPhrase();
            trainingPhrase.setPhraseText(phrase);
            trainingPhrase.setIsProcessed(true);
            trainingPhrase.setIntent(intent);
            chatTrainingPhraseRepository.save(trainingPhrase);
        }

        // Response with database query
        ChatResponse response = new ChatResponse();
        response.setResponseText("Hiện tại có {{job_count}} công việc yêu cầu kỹ năng {{skill}}. Một số công việc tiêu biểu là: {{title SEPARATOR '}}.");
        response.setDisplayOrder(0);
        response.setIntent(intent);
        response.setRequiresDbQuery(true);
        response.setQueryTemplate(
                "SELECT " +
                        "s.skill_name as skill, " +
                        "COUNT(DISTINCT j.id) as job_count, " +
                        "SUBSTRING_INDEX(GROUP_CONCAT(DISTINCT j.title SEPARATOR ', '), ', ', 3) as sample_jobs " +
                        "FROM job j " +
                        "JOIN job_skill js ON j.id = js.job_id " +
                        "JOIN skill s ON js.skill_id = s.id " +
                        "WHERE LOWER(s.skill_name) LIKE LOWER('%{{skill}}%') " +
                        "GROUP BY s.skill_name " +
                        "LIMIT 1"
        );
        chatResponseRepository.save(response);
        ChatResponse fallbackResponse = new ChatResponse();
        fallbackResponse.setResponseText("Hiện tại tôi không tìm thấy công việc nào cho kỹ năng {{skill}} trong cơ sở dữ liệu. Tuy nhiên, đây là một kỹ năng có tiềm năng và bạn có thể thử tìm kiếm với từ khóa tương tự hoặc xem các kỹ năng đang hot nhất hiện nay.");
        fallbackResponse.setDisplayOrder(1);
        fallbackResponse.setIntent(intent);
        fallbackResponse.setRequiresDbQuery(false);
        chatResponseRepository.save(fallbackResponse);
    }

    private void createTrendingSkillsIntent() {
        ChatIntent intent = new ChatIntent();
        intent.setIntentName("trending_skills");
        intent.setDescription("Kỹ năng đang hot và được săn đón");
        intent = chatIntentRepository.save(intent);

        // Training phrases
        List<String> trainingPhrases = Arrays.asList(
                "Những kỹ năng nào đang hot?",
                "Kỹ năng nào đang được săn đón?",
                "Top kỹ năng có nhiều công việc nhất",
                "Kỹ năng nào đang được yêu cầu nhiều nhất?",
                "Kỹ năng phổ biến hiện nay",
                "Những skill nào đang được săn đón nhiều nhất?",
                "Kỹ năng nào nhiều việc làm nhất?",
                "Đâu là những kỹ năng được cần nhiều nhất?",
                "Xu hướng kỹ năng hiện nay",
                "Skill nào đang hot trên thị trường?"
        );

        for (String phrase : trainingPhrases) {
            ChatTrainingPhrase trainingPhrase = new ChatTrainingPhrase();
            trainingPhrase.setPhraseText(phrase);
            trainingPhrase.setIsProcessed(true);
            trainingPhrase.setIntent(intent);
            chatTrainingPhraseRepository.save(trainingPhrase);
        }

        // Response with database query - Sử dụng subqueries thay vì CTE cho tương thích với MySQL 5.x
        ChatResponse response = new ChatResponse();
        response.setResponseText("Dựa trên dữ liệu của chúng tôi, những kỹ năng đang hot nhất hiện nay là: {{GROUP_CONCAT(skill_name ORDER BY job_count DESC SEPARATOR '}}. Trong đó, {{top_skills}} đang được yêu cầu trong nhiều công việc nhất với {{(SELECT skill_name}} công việc hiện có.\n" +
                "\n");
        response.setDisplayOrder(0);
        response.setIntent(intent);
        response.setRequiresDbQuery(true);
        response.setQueryTemplate(
                "SELECT " +
                        "GROUP_CONCAT(skill_name ORDER BY job_count DESC SEPARATOR ', ') AS top_skills, " +
                        "(SELECT skill_name FROM " +
                        "    (SELECT s.skill_name, COUNT(js.job_id) as job_count " +
                        "     FROM skill s " +
                        "     JOIN job_skill js ON s.id = js.skill_id " +
                        "     GROUP BY s.skill_name " +
                        "     ORDER BY job_count DESC " +
                        "     LIMIT 1) AS top_single" +
                        ") AS top_skill, " +
                        "(SELECT job_count FROM " +
                        "    (SELECT s.skill_name, COUNT(js.job_id) as job_count " +
                        "     FROM skill s " +
                        "     JOIN job_skill js ON s.id = js.skill_id " +
                        "     GROUP BY s.skill_name " +
                        "     ORDER BY job_count DESC " +
                        "     LIMIT 1) AS top_count" +
                        ") AS job_count " +
                        "FROM " +
                        "    (SELECT s.skill_name, COUNT(js.job_id) as job_count " +
                        "     FROM skill s " +
                        "     JOIN job_skill js ON s.id = js.skill_id " +
                        "     GROUP BY s.skill_name " +
                        "     ORDER BY job_count DESC " +
                        "     LIMIT 5) AS skill_data"
        );
        chatResponseRepository.save(response);
    }

    private void createPopularCategoriesIntent() {
        ChatIntent intent = new ChatIntent();
        intent.setIntentName("popular_categories");
        intent.setDescription("Ngành nghề và danh mục công việc đang hot");
        intent = chatIntentRepository.save(intent);

        // Training phrases
        List<String> trainingPhrases = Arrays.asList(
                "Ngành nghề nào đang hot?",
                "Danh mục công việc phổ biến nhất",
                "Lĩnh vực nào đang cần nhiều người?",
                "Ngành nào đang thiếu nhân lực?",
                "Những ngành có nhiều việc làm nhất",
                "Danh mục công việc hot nhất hiện nay",
                "Lĩnh vực nào đang phát triển?",
                "Top ngành nghề hot nhất",
                "Ngành nào đang có nhu cầu cao?",
                "Xu hướng ngành nghề hiện nay"
        );

        for (String phrase : trainingPhrases) {
            ChatTrainingPhrase trainingPhrase = new ChatTrainingPhrase();
            trainingPhrase.setPhraseText(phrase);
            trainingPhrase.setIsProcessed(true);
            trainingPhrase.setIntent(intent);
            chatTrainingPhraseRepository.save(trainingPhrase);
        }

        // Response with database query - Sử dụng subqueries thay vì CTE cho tương thích với MySQL 5.x
        ChatResponse response = new ChatResponse();
        response.setResponseText("Những ngành nghề đang hot nhất trên nền tảng của chúng tôi là: {{GROUP_CONCAT(category_title ORDER BY job_count DESC SEPARATOR '}}. Đặc biệt, ngành {{top_categories}} đang dẫn đầu với {{(SELECT category_title}} công việc hiện có.");
        response.setDisplayOrder(0);
        response.setIntent(intent);
        response.setRequiresDbQuery(true);
        response.setQueryTemplate(
                "SELECT GROUP_CONCAT(category_title ORDER BY job_count DESC SEPARATOR ', ') AS top_categories, " +
                        "(SELECT category_title FROM " +
                        "    (SELECT c.category_title, COUNT(j.id) as job_count " +
                        "     FROM category c " +
                        "     JOIN job j ON c.id = j.category_id " +
                        "     WHERE j.status = 'POSTED' " +
                        "     GROUP BY c.category_title " +
                        "     ORDER BY job_count DESC " +
                        "     LIMIT 1) AS top_single) AS top_category, " +
                        "(SELECT job_count FROM " +
                        "    (SELECT c.category_title, COUNT(j.id) as job_count " +
                        "     FROM category c " +
                        "     JOIN job j ON c.id = j.category_id " +
                        "     WHERE j.status = 'POSTED' " +
                        "     GROUP BY c.category_title " +
                        "     ORDER BY job_count DESC " +
                        "     LIMIT 1) AS top_count) AS job_count " +
                        "FROM " +
                        "    (SELECT c.category_title, COUNT(j.id) as job_count " +
                        "     FROM category c " +
                        "     JOIN job j ON c.id = j.category_id " +
                        "     WHERE j.status = 'POSTED' " +
                        "     GROUP BY c.category_title " +
                        "     ORDER BY job_count DESC " +
                        "     LIMIT 5) AS cat_data;"
        );

        chatResponseRepository.save(response);
    }

    private void createJobCountByCategoryIntent() {
        ChatIntent intent = new ChatIntent();
        intent.setIntentName("job_count_by_category");
        intent.setDescription("Đếm số lượng công việc theo danh mục");
        intent = chatIntentRepository.save(intent);

        // Training phrases
        List<String> trainingPhrases = Arrays.asList(
                "Hiện tại có bao nhiêu công việc với danh mục {category}?",
                "Có bao nhiêu việc làm trong ngành {category}?",
                "Đếm số công việc trong lĩnh vực {category}",
                "Số lượng công việc cho ngành {category}",
                "Mấy công việc thuộc ngành {category}?",
                "{category} có bao nhiêu công việc?",
                "Đếm việc làm ngành {category}",
                "Tổng số việc làm thuộc {category}",
                "Có bn công việc thuộc lĩnh vực {category}?",
                "Việc làm ngành {category} hiện có bao nhiêu?"
        );

        for (String phrase : trainingPhrases) {
            ChatTrainingPhrase trainingPhrase = new ChatTrainingPhrase();
            trainingPhrase.setPhraseText(phrase);
            trainingPhrase.setIsProcessed(true);
            trainingPhrase.setIntent(intent);
            chatTrainingPhraseRepository.save(trainingPhrase);
        }

        // Response with database query
        ChatResponse response = new ChatResponse();
        response.setResponseText("Hiện tại có {{job_count}} công việc trong danh mục {{category}}. Một số công việc tiêu biểu là: {{title SEPARATOR '}}.");
        response.setDisplayOrder(0);
        response.setIntent(intent);
        response.setRequiresDbQuery(true);
        response.setQueryTemplate(
                "SELECT " +
                        "c.category_title AS category, " +
                        "COUNT(j.id) AS job_count, " +
                        "SUBSTRING_INDEX(GROUP_CONCAT(DISTINCT j.title SEPARATOR ', '), ', ', 3) AS sample_jobs " +
                        "FROM job j " +
                        "JOIN category c ON j.category_id = c.id " +
                        "WHERE LOWER(c.category_title) LIKE LOWER('%{{category}}%') " +
                        "GROUP BY c.category_title " +
                        "LIMIT 1"
        );
        chatResponseRepository.save(response);

        // Fallback response
        ChatResponse fallbackResponse = new ChatResponse();
        fallbackResponse.setResponseText("Tôi không tìm thấy danh mục {{category}} trong cơ sở dữ liệu. Có thể bạn muốn tìm kiếm với tên danh mục khác hoặc xem danh sách tất cả các danh mục trên trang chủ của chúng tôi.");
        fallbackResponse.setDisplayOrder(1);
        fallbackResponse.setIntent(intent);
        fallbackResponse.setRequiresDbQuery(false);
        chatResponseRepository.save(fallbackResponse);
    }

    private void createHelpIntent() {
        ChatIntent intent = new ChatIntent();
        intent.setIntentName("help");
        intent.setDescription("Trợ giúp chung");
        intent = chatIntentRepository.save(intent);

        // Training phrases
        List<String> trainingPhrases = Arrays.asList(
                "Bạn có thể giúp gì cho tôi",
                "Tôi cần trợ giúp",
                "Bạn làm được gì",
                "Chatbot này có chức năng gì",
                "Những câu hỏi có thể hỏi",
                "Bạn trả lời được câu hỏi nào",
                "Hướng dẫn sử dụng bot",
                "Có thể hỏi những gì",
                "Giúp đỡ",
                "Tôi không biết hỏi gì"
        );

        for (String phrase : trainingPhrases) {
            ChatTrainingPhrase trainingPhrase = new ChatTrainingPhrase();
            trainingPhrase.setPhraseText(phrase);
            trainingPhrase.setIsProcessed(true);
            trainingPhrase.setIntent(intent);
            chatTrainingPhraseRepository.save(trainingPhrase);
        }

        // Responses
        List<String> responses = Arrays.asList(
                "Tôi có thể giúp bạn với các câu hỏi như:\n" +
                        "- \"Tôi có kỹ năng Java, Python thì có công việc nào phù hợp không?\"\n" +
                        "- \"Có công việc nào cho skill React hay không?\"\n" +
                        "- \"Ngành nghề nào đang hot?\"\n" +
                        "- \"Những skill nào đang được săn đón nhiều nhất?\"\n" +
                        "- \"Hiện tại có bao nhiêu công việc với skill JavaScript?\"\n" +
                        "- \"Hiện tại có bao nhiêu công việc với danh mục IT?\"",

                "Bạn có thể hỏi tôi về thông tin công việc theo kỹ năng, ngành nghề đang hot, kỹ năng được săn đón nhiều, hoặc số lượng công việc theo kỹ năng và danh mục. Ví dụ như \"Những skill nào đang hot?\" hoặc \"Tôi có kỹ năng Python, có công việc nào phù hợp không?\""
        );

        int order = 0;
        for (String response : responses) {
            ChatResponse chatResponse = new ChatResponse();
            chatResponse.setResponseText(response);
            chatResponse.setDisplayOrder(order++);
            chatResponse.setIntent(intent);
            chatResponseRepository.save(chatResponse);
        }
    }
}