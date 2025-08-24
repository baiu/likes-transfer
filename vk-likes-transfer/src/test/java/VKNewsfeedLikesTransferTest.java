import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.newsfeed.responses.GetResponse;
import com.vk.api.sdk.oneofs.NewsfeedNewsfeedItemOneOf;
import com.vk.api.sdk.queries.fave.FaveAddPostQuery;
import com.vk.api.sdk.queries.newsfeed.NewsfeedGetQuery;
import com.vk.api.sdk.queries.fave.Fave;
import com.vk.api.sdk.queries.newsfeed.Newsfeed;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.mockito.Mockito.*;

public class VKNewsfeedLikesTransferTest {
    @Test
    void transferLikesCallsFaveAddPostWithCorrectIds() throws Exception {
        VkApiClient vk = mock(VkApiClient.class);
        Newsfeed newsfeed = mock(Newsfeed.class);
        Fave fave = mock(Fave.class);
        NewsfeedGetQuery getQuery = mock(NewsfeedGetQuery.class);
        FaveAddPostQuery addPostQuery = mock(FaveAddPostQuery.class);
        GetResponse response = mock(GetResponse.class);
        NewsfeedNewsfeedItemOneOf item = mock(NewsfeedNewsfeedItemOneOf.class, RETURNS_DEEP_STUBS);

        UserActor fromActor = new UserActor(1, "token1");
        UserActor toActor = new UserActor(2, "token2");
        int ownerId = 123;
        int postId = 55;

        when(vk.newsfeed()).thenReturn(newsfeed);
        when(newsfeed.get(fromActor)).thenReturn(getQuery);
        when(getQuery.section("likes")).thenReturn(getQuery);
        when(getQuery.execute()).thenReturn(response);

        when(response.getItems()).thenReturn(Collections.singletonList(item));
        when(item.getOneOf0().getPostId()).thenReturn(postId);

        when(vk.fave()).thenReturn(fave);
        when(fave.addPost(toActor, ownerId, postId)).thenReturn(addPostQuery);

        VKNewsfeedLikesTransfer.transferLikes(vk, fromActor, toActor, ownerId);

        verify(fave).addPost(toActor, ownerId, postId);
        verify(addPostQuery).execute();
    }
}
