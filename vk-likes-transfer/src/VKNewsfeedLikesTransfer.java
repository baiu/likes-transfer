import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.newsfeed.responses.GetResponse;
import com.vk.api.sdk.oneofs.NewsfeedNewsfeedItemOneOf;
import com.vk.api.sdk.queries.fave.FaveAddPostQuery;
import com.vk.api.sdk.queries.newsfeed.NewsfeedGetQuery;

public class VKNewsfeedLikesTransfer {

        public static void main(String[] args) {
            TransportClient transportClient = new HttpTransportClient();
            VkApiClient vk = new VkApiClient(transportClient);

            // Specify your VK application access token here
            String accessToken = "YOUR_ACCESS_TOKEN";
            // ID of the user whose liked posts should be added to bookmarks
            int targetUserId = 123456789; // Replace with the actual ID

            int targetUserId2 = 123456788; // Replace with the actual ID

            UserActor userActor = new UserActor(targetUserId, accessToken);
            UserActor userActor2 = new UserActor(targetUserId2, accessToken);

            NewsfeedGetQuery newsfeedGetQuery = vk.newsfeed().get(userActor);
            newsfeedGetQuery.section("likes");
            System.out.println("List of liked posts:");
            try {
                GetResponse response = newsfeedGetQuery.execute();
                for (NewsfeedNewsfeedItemOneOf item : response.getItems()) {
                    System.out.println(item.toString());

                    // Добавление поста в закладки
                    FaveAddPostQuery addPostQuery = vk.fave().addPost(userActor2, targetUserId, item.getOneOf0().getPostId());
                    try {
                        addPostQuery.execute();
                        System.out.println("Post successfully added to bookmarks");
                    } catch (ApiException e) {
                        System.out.println("Failed to add the post to bookmarks: " + e.getMessage());
                    }
                }
            } catch (ApiException | ClientException e) {
                System.out.println("An error occurred: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }