package cn.scau.mouzhi.atys;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import cn.scau.mouzhi.aty.R;

/***
 * 
 * Created by yetwish on 2015-05-11
 */

public class mySearchView extends LinearLayout implements View.OnClickListener {  
	  
    /** 
     * 杈撳叆妗� 
     */  
    private EditText etInput;  
  
    /** 
     * 鍒犻櫎閿� 
     */  
    private ImageView ivDelete;  
  
    /** 
     * 杩斿洖鎸夐挳 
     */  
    private Button btnBack;  
  
    /** 
     * 涓婁笅鏂囧璞� 
     */  
    private Context mContext;  
  
    /** 
     * 寮瑰嚭鍒楄〃 
     */  
    private ListView lvTips;  
  
    /** 
     * 鎻愮ずadapter 锛堟帹鑽恆dapter锛� 
     */  
    private ArrayAdapter<String> mHintAdapter;  
  
    /** 
     * 鑷姩琛ュ叏adapter 鍙樉绀哄悕瀛� 
     */  
    private ArrayAdapter<String> mAutoCompleteAdapter;  
  
    /** 
     * 鎼滅储鍥炶皟鎺ュ彛 
     */  
    private SearchViewListener mListener;  
  
    /** 
     * 璁剧疆鎼滅储鍥炶皟鎺ュ彛 
     * 
     * @param listener 鐩戝惉鑰� 
     */  
    public void setSearchViewListener(SearchViewListener listener) {  
        mListener = listener;  
    }  
  
    public mySearchView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        mContext = context;  
        LayoutInflater.from(context).inflate(R.layout.search_layout, this);  
        initViews();  
    }  
  
    private void initViews() {  
        etInput = (EditText) findViewById(R.id.search_et_input);  
        ivDelete = (ImageView) findViewById(R.id.search_iv_delete);  
        btnBack = (Button) findViewById(R.id.search_btn_back);  
//        lvTips = (ListView) findViewById(R.id.search_lv_tips);  
//  
//        lvTips.setOnItemClickListener(new AdapterView.OnItemClickListener() {  
//            @Override  
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {  
//                //set edit text  
//                String text = lvTips.getAdapter().getItem(i).toString();  
//                etInput.setText(text);  
//                etInput.setSelection(text.length());  
//                //hint list view gone and result list view show  
//                lvTips.setVisibility(View.GONE);  
//                notifyStartSearching(text);  
//            }  
//        });  
  
        ivDelete.setOnClickListener(this);  
        btnBack.setOnClickListener(this);  
  
//        etInput.addTextChangedListener(new EditChangedListener());  
//        etInput.setOnClickListener(this);  
//        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {  
//            @Override  
//            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {  
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {  
//                    lvTips.setVisibility(GONE);  
//                    notifyStartSearching(etInput.getText().toString());  
//                }  
//                return true;  
//            }  
//        });  
    }  
  
    /** 
     * 閫氱煡鐩戝惉鑰� 杩涜鎼滅储鎿嶄綔 
     * @param text 
     */  
    private void notifyStartSearching(String text){  
        if (mListener != null) {  
            mListener.onSearch(etInput.getText().toString());  
        }  
        //闅愯棌杞敭鐩�  
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);  
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
    }  
  
    /** 
     * 璁剧疆鐑悳鐗堟彁绀� adapter 
     */  
    public void setTipsHintAdapter(ArrayAdapter<String> adapter) {  
        this.mHintAdapter = adapter;  
        if (lvTips.getAdapter() == null) {  
            lvTips.setAdapter(mHintAdapter);  
        }  
    }  
  
    /** 
     * 璁剧疆鑷姩琛ュ叏adapter 
     */  
    public void setAutoCompleteAdapter(ArrayAdapter<String> adapter) {  
        this.mAutoCompleteAdapter = adapter;  
    }  
  
    private class EditChangedListener implements TextWatcher {  
        @Override  
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {  
  
        }  
  
        @Override  
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {  
            if (!"".equals(charSequence.toString())) {  
                ivDelete.setVisibility(VISIBLE);  
                lvTips.setVisibility(VISIBLE);  
                if (mAutoCompleteAdapter != null && lvTips.getAdapter() != mAutoCompleteAdapter) {  
                    lvTips.setAdapter(mAutoCompleteAdapter);  
                }  
                //鏇存柊autoComplete鏁版嵁  
                if (mListener != null) {  
                    mListener.onRefreshAutoComplete(charSequence + "");  
                }  
            } else {  
                ivDelete.setVisibility(GONE);  
                if (mHintAdapter != null) {  
                    lvTips.setAdapter(mHintAdapter);  
                }  
                lvTips.setVisibility(GONE);  
            }  
        }  
  
        @Override  
        public void afterTextChanged(Editable editable) {  
        }  
    }  
  
    @Override  
    public void onClick(View view) {  
        switch (view.getId()) {  
            case R.id.search_et_input:  
//                lvTips.setVisibility(VISIBLE);  
                break;  
            case R.id.search_iv_delete:  
                etInput.setText("");  
                ivDelete.setVisibility(GONE);  
                break;  
            case R.id.search_btn_back:  
            	lvTips.setVisibility(GONE);  
                ((Activity) mContext).finish();  
                break;  
        }  
    }  
  
    /** 
     * search view鍥炶皟鏂规硶 
     */  
    public interface SearchViewListener {  
  
        /** 
         * 鏇存柊鑷姩琛ュ叏鍐呭 
         * 
         * @param text 浼犲叆琛ュ叏鍚庣殑鏂囨湰 
         */  
        void onRefreshAutoComplete(String text);  
  
        /** 
         * 寮�濮嬫悳绱� 
         * 
         * @param text 浼犲叆杈撳叆妗嗙殑鏂囨湰 
         */  
        void onSearch(String text);  
  
//        /**  
//         * 鎻愮ず鍒楄〃椤圭偣鍑绘椂鍥炶皟鏂规硶 (鎻愮ず/鑷姩琛ュ叏)  
//         */  
//        void onTipsItemClick(String text);  
    }  
  
}  