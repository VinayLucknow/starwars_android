package adapter;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.arrk.starwarcharacters.R;
import java.util.List;
import model.Character;
import model.CharactersList;
import util.EmptyRecyclerView;
import util.OnLoadMoreListener;
import util.ReachRecyclerView;
import util.ServiceListener;


public class CharacterListAdapter extends ReachRecyclerView.Adapter{

    private Activity activity;
    private List<Character> characters;
    private String next;
    private int count;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private int page = 1;



    public CharacterListAdapter(Activity activity, ReachRecyclerView recyclerView, List<Character> characters, final String next, final int count) {
        this.activity = activity;
        this.characters = characters;
        this.next = next;
        this.count = count;
        if(recyclerView.getLayoutManager() instanceof LinearLayoutManager){
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold) && totalItemCount < count) {
                        if (onLoadMoreListener != null && next!=null) {
                            page = page+1;
                            onLoadMoreListener.onLoadMore(page);
                        }
                        isLoading = true;
                    }

                }
            });
        }
    }

    private OnLoadMoreListener onLoadMoreListener;
    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    private ServiceListener<Character> characterSelectedListener;
    public void setCustomerSelectedListener(ServiceListener<Character> customerSelectedListener) {
        this.characterSelectedListener = customerSelectedListener;
    }

    @Override
    public ReachRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM){
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_characters, parent, false);
            return new CharacterViewHolder(itemView);
        }else if(viewType == VIEW_PROG){
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(itemView);
        }

        return null;
    }

    
    @Override
    public void onBindViewHolder(ReachRecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof CharacterViewHolder){
            Character character = characters.get(position);
            CharacterViewHolder characterViewHolder = (CharacterViewHolder)holder;
            characterViewHolder.characterName.setText(character.getName());
            characterViewHolder.characterName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(characterSelectedListener!=null){

                        characterSelectedListener.result(characters.get(position));
                    }
                }
            });
        }else if(holder instanceof LoadingViewHolder){

            LoadingViewHolder loadingViewHolder = (LoadingViewHolder)holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }

    }

    // "Loading item" ViewHolder
    private class LoadingViewHolder extends ReachRecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }


    public class CharacterViewHolder extends ReachRecyclerView.ViewHolder {
        
        public TextView characterName;
        public View convertView;

        public CharacterViewHolder(View view) {
            super(view);
            convertView = view;
            characterName = (TextView) view.findViewById(R.id.item_character_tv_name);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return characters.get(position) !=null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void setNextUrl(String nextUrl){

        next = nextUrl;
    }
}
    



