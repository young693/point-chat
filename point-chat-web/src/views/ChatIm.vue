<template>
  <div class="chat-container">
    <!-- 左侧导航栏 -->
    <div class="nav-side">
      <!-- 导航按钮 -->
      <div class="nav-items">
        <!-- 用户资料 -->

        <div class="nav-item" :class="{ active: activeTab === 'profile' }">
          <el-dropdown>
            <el-avatar :size="32" :src="loginUser.avatar" class="user-avatar" />
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="showUserCard(loginUser)">个人信息</el-dropdown-item>
                <el-dropdown-item @click="loginout">退出</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>


        <!-- 消息 -->
        <div class="nav-item" :class="{ active: activeTab === 'message' }" @click="updateTab('message')">
          <el-icon :size="26" class="nav-icon">
            <ChatDotRound />
          </el-icon>
          <el-badge v-if="totalUnread > 0" :value="totalUnread" class="nav-badge" />
        </div>

        <!-- 智能机器人 -->
        <!-- <div class="nav-item" :class="{ active: activeTab === 'chatbot' }" @click="updateTab('chatbot')"> -->
        <!-- <el-icon :size="26" class="nav-icon"> -->
        <!-- 替换为专用机器人图标 -->
        <!-- <MagicStick /> -->
        <!-- </el-icon> -->
        <!-- <el-badge v-if="totalUnread > 0" :value="totalUnread" class="nav-badge" /> -->
        <!-- </div> -->

        <!-- 通讯录 -->
        <div class="nav-item" :class="{ active: activeTab === 'contact' }" @click="updateTab('contact')">
          <el-icon :size="26" class="nav-icon">
            <User />
          </el-icon>
          <!-- 未读消息徽章 -->
          <el-badge :value="unreadApply" v-if="unreadApply > 0" class="message-badge" />
        </div>

        <!-- 朋友圈 -->
        <div class="nav-item" :class="{ active: activeTab === 'moment' }" @click="updateTab('moment')">
          <el-icon :size="26" class="nav-icon">
            <Picture />
          </el-icon>
        </div>

        <!-- 设置 -->
        <div class="nav-item" :class="{ active: activeTab === 'settings' }" @click="updateTab('settings')">
          <el-icon :size="26" class="nav-icon">
            <Setting />
          </el-icon>
        </div>
      </div>
    </div>
    <!-- Left side: User list -->
    <div class="left-side">
      <!-- 联系人 -->
      <template v-if="activeTab === 'profile'">
        <div class="dialog-mask">
          <div class="user-card">

            <div class="main-content">
              <!-- 关闭按钮 -->
              <div class="close-btn" @click="closeDialog">×</div>

              <!-- 用户信息区域 -->
              <div class="card-header">
                <img :src="loginUser.avatar" class="contact-avatar" />
                <div class="user-info">
                  <h3>{{ loginUser.nickname }}</h3>

                  <p>{{ loginUser.sex === 0 ? "男" : "女" }}</p>

                </div>
              </div>

              <div class="detail-section">

                <div class="detail-item">
                  <span>手机号</span>
                  <span>{{ loginUser.phone }}</span>
                </div>
                <div class="detail-item">
                  <span>个性签名</span>
                  <span>{{ loginUser.signature || '暂无个性签名' }}</span>
                </div>
              </div>
              <el-button style="background-color: #409EFF; color: white; width: 100px; height: 40px; padding: 0;"
                @click="handleInfo">修改资料</el-button>
            </div>
          </div>
        </div>
      </template>

      <template v-if="activeTab === 'info'">
        <div class="dialog-mask">
          <div class="user-card">
            <div class="main-content">
              <div class="close-btn" @click="activeTab = 'profile'">×</div>
              <h2 class="edit-title">修改资料</h2>

              <div class="avatar-section">
                <img :src="editForm.avatar" style="width: 100px; height: 100px;" />
                <br>
                <el-button @click="triggerAvatarInput" style="background-color: #32CD32; color: white; border: none; border-radius: 5px; 
                  padding: 10px 20px; font-size: 14px; cursor: pointer; transition: background-color 0.3s;">
                  选择头像
                  <input ref="avatarInput" type="file" class="hidden-file" @change="handleAvatarChange">
                </el-button>
              </div>

              <!-- 表单区域 -->
              <div class="form-section">

                <div class="form-item">
                  <label>性别</label>
                  <el-radio-group v-model="editForm.sex">
                    <el-radio :label="0">男</el-radio>
                    <el-radio :label="1">女</el-radio>
                  </el-radio-group>
                </div>

                <div class="form-item">
                  <label>昵称</label>
                  <el-input v-model="editForm.nickname" placeholder="请输入新昵称"
                    style="color: red !important; background: yellow !important"></el-input>
                </div>

                <div class="form-item">
                  <label>个性签名</label>
                  <el-input type="textarea" v-model="editForm.signature" placeholder="请输入个性签名"
                    style="color: red !important; background: yellow !important"></el-input>
                </div>

                <!-- 保存按钮 -->
                <el-button type="primary" @click="submitForm"
                  style="background-color: #32CD32; color: white; border: none; 
                border-radius: 5px; padding: 10px 20px; font-size: 14px; cursor: pointer; transition: background-color 0.3s;">
                  保存修改
                </el-button>
              </div>


            </div>
          </div>
        </div>
      </template>


      <template v-if="activeTab === 'contact'">
        <div class="contact-header">
          <!-- 搜索框区域 -->
          <div class="search-box">
            <el-input v-model="contactSearch" placeholder="微信号/手机号" clearable @keyup.enter.native="handleSearchUser"
              class="contact-search-input">
              <i slot="suffix" class="el-icon-plus search-icon" @click="handleSearchUser"></i>
            </el-input>

            <!-- 搜索结果悬浮层 -->
            <div v-if="showResultLayer" class="result-layer">
              <div v-for="member in searchResult" class="user-item" @click="showMemberCard(member)">
                <img :src="member.avatar" class="contact-avatar" />
                <div class="user-name">{{ member.nickname }}：</div>
                <!-- <div class="user-phone">{{ searchResult.phone }}</div> -->
                <div class="user-sex">
                  <i :class="['sex-icon', member.sex === 0 ? 'el-icon-male' : 'el-icon-female']"></i>
                  <span>{{ member.sex === 1 ? '男' : '女' }}</span>
                </div>

              </div>
              <!-- <div v-else class="empty-tip">
                用户不存在
              </div> -->
            </div>
          </div>

          <!-- 用户卡片弹窗 -->
          <div v-if="showUserDialog" class="dialog-mask">
            <div class="user-card">
              <div v-if="!showAddFriendForm" class="main-content">
                <!-- 关闭按钮 -->
                <div class="close-btn" @click="closeDialog">×</div>

                <!-- 用户信息区域 -->
                <div class="card-header">
                  <img :src="currentSearchUser.avatar" class="contact-avatar" />
                  <div class="user-info">
                    <h3>{{ currentSearchUser.nickname }}</h3>
                    <p class="signature">{{ currentSearchUser.signature || '暂无个性签名' }}</p>
                  </div>
                </div>

                <!-- 详细信息 -->
                <div class="detail-section">
                  <div class="detail-item">
                    <span>编号</span>
                    <span>{{ currentSearchUser.id }}</span>
                  </div>
                  <!-- <div v-if="isFriend" class="detail-item">
                    <span>朋友圈</span>
                    <span>{{ currentSearchUser.moments || 0 }}条动态</span>
                  </div> -->
                </div>

                <!-- 操作按钮 -->
                <div class="action-buttons">
                  <template v-if="isFriend">
                    <button class="btn primary" @click="handleSelectUser(currentSearchUser)">发消息</button>
                    <button class="btn">视频通话</button>
                    <button class="btn">语音通话</button>
                  </template>
                  <button v-else class="btn add-friend" @click="enterAddFriend">加好友</button>
                </div>

              </div>
              <!-- 好友申请表单 -->
              <div v-else class="friend-form">
                <div class="form-header">
                  <el-icon class="back-icon" @click="showAddFriendForm = false">
                    <ArrowLeft />
                  </el-icon>
                  <h3>好友申请</h3>
                </div>

                <div class="form-body">
                  <el-input v-model="applyReason" type="textarea" :rows="4" placeholder="请输入申请理由（最多50字）" maxlength="50"
                    show-word-limit class="reason-input" />

                  <div class="form-actions">
                    <button class="btn submit-btn" :disabled="!applyReason" @click="handleSubmitApply">
                      发送申请
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>



        </div>

        <el-scrollbar class="contact-list-scroll">
          <!-- 新朋友固定按钮 -->
          <div class="new-friend-item" @click="showNewFriendList">
            <el-icon class="new-friend-icon">
              <User />
              <el-badge :value="unreadApply" v-if="unreadApply > 0" class="message-badge" />
            </el-icon>
            <span>新的朋友<!-- 未读消息徽章 -->
            </span>
          </div>

          <!-- 联系人列表 -->
          <div v-for="contact in filteredContacts" :key="contact.friendId" class="contact-item"
            @click="selectContact(contact)">
            <img :src="contact.avatar" class="contact-avatar" />
            <span class="contact-name">{{ contact.nickname }}</span>
          </div>
        </el-scrollbar>

        <!-- 新好友弹窗组件 -->
        <!-- 申请列表弹窗 -->
        <div v-if="showDialog" class="dialog-mask">
          <!--          <div class="user-card"> @click="closeDialog"></div>-->
          <!--333-->
          <div class="dialog-content">
            <div class="close-btn" @click="closeApply">×</div>
            <h3 class="dialog-title">好友申请</h3>

            <div class="application-list">
              <div v-for="(item, index) in friendApplications" :key="index" class="application-item">
                <!--      点击可以查看用户信息          -->
                <div class="user-avatar" @click="">
                  <img :src="item.avatar" alt="avatar" />
                </div>

                <div class="user-info">
                  <div class="name-row">
                    <span class="username">{{ item.nickname }}</span>
                    <!-- <span class="wechat-id">手机号：{{ item.friendId  }}</span> -->
                  </div>
                  <p class="apply-reason">{{ item.reason }}</p>
                </div>

                <div class="action-buttons">

                  <button v-if="item.status === 0" class="btn accept" @click="handleApply(item, true)">
                    同意
                  </button>
                  <button v-if="item.status === 0" class="btn reject" @click="handleApply(item, false)">
                    拒绝
                  </button>
                  <!--                  :class="['status-tag']"-->
                  <span v-else :class="['status-tag', item.status]">
                    {{ item.status === 1 ? '已通过' : '已拒绝' }}
                  </span>
                </div>
              </div>
              <div v-if="friendApplications.length <= 0 && applicaionFriends.lnegth <= 0"> 暂无好友申请 </div>

              <div v-for="(item, index) in applicaionFriends" :key="index" class="application-item">
                <div class="user-avatar" @click="">
                  <img :src="item.avatar" alt="avatar" />
                </div>

                <div class="user-info">
                  <div class="name-row">
                    <span class="username">{{ item.nickname }}</span>
                    <!-- <span class="wechat-id">手机号：{{ item.friendId  }}</span> -->
                  </div>
                  <p class="apply-reason">{{ item.reason }}</p>
                </div>
                <span :class="['status-tag', item.status]">
                  {{ item.status === 1 ? '已通过' : item.status === 0 ? '等待验证' : "被拒绝" }}
                </span>
              </div>

            </div>
          </div>
        </div>

      </template>
      <template v-if="activeTab === 'message'">

        <!--        搜索框和消息加上统一容器-->
        <div class="left-chat-container">

          <!-- Search input (moved outside) -->
          <div class="search-wrapper">
            <!--        <el-input v-model="searchUserName" placeholder="回车搜索用户" class="search-input" @keydown.enter.native="searchUserForForm"></el-input>-->
            <input type="text" v-model="searchUserName" placeholder="回车搜索用户" class="search-input"
              @keydown.enter="searchUserForForm" @input="handleSearchInput">

            <!-- 新增独立图标按钮 -->
            <button class="add-btn" @click="newChatGroup">+</button>
          </div>
          <!-- User list (with scroll) -->
          <el-scrollbar class="user-list-scroll">
            <el-row>

              <el-col :span="24" v-for="(form, index) in curAllMessage" :key="index"
                @click="handleItemClick(form, index)" :class="{ 'active-item': activeIndex === index }"
                class="user-item" v-if="messageForm.length !== 0">
                <div class="user-avatar-wrapper">
                  <!-- 方形头像 -->
                  <img :src="form.avatar" class="user-avatar">

                  <!-- 未读消息徽章 -->
                  <!-- <el-badge
                      :value="form.unreadCount"
                      v-if="form.  > 0"
                      class="message-badge"
                  /> -->

                  <!--              &lt;!&ndash; 在线状态指示 &ndash;&gt;-->
                  <!--              <div-->
                  <!--                  v-if="form.recieiveUser.isOnline"-->
                  <!--                  class="online-dot"-->
                  <!--              ></div>-->
                </div>

                <div class="user-details">
                  <!-- 头部行容器 -->
                  <div class="header-line">
                    <div class="user-name ellipsis">{{ form.name }}</div>
                    <div class="message-time">{{ formatTime(form.sendTime) }}</div>
                  </div>

                  <!-- 最后消息 -->
                  <div class="last-message ellipsis">
                    <span :class="['last-message', { truncate: form.lastContent.length > 6 }]">
                      {{ form.lastContent }}
                    </span>：
                    {{ form.chatType || "暂无消息" }}
                  </div>
                </div>


              </el-col>
            </el-row>
            <!-- 搜索结果悬浮层 -->
            <transition name="fade">
              <el-scrollbar v-show="showSearchResult" class="user-list-scroll-search"
                :class="{ 'search-active': showSearchResult }">
                <el-row>
                  <el-col :span="24" v-for="form in searchMessageForm" :key="form.id"
                    @click.native="handleSelectUser(form)" class="user-item">
                    <!-- 用户项结构（同原有内容） -->
                    <div class="user-avatar-wrapper">
                      <!-- 方形头像 -->
                      <img :src="form.avatar" class="user-avatar">

                      <!-- 未读消息徽章 -->
                      <!-- <el-badge
                          :value="form.noReadMessageLength"
                          v-if="form.noReadMessageLength > 0"
                          class="message-badge"
                      /> -->

                      <!-- 在线状态指示 -->
                      <!-- <div
                          v-if="form.recieiveUser.isOnline"
                          class="online-dot"
                      ></div> -->
                    </div>

                    <div class="user-details">
                      <div class="header-line">
                        <div class="user-name">{{ form.nickname }}</div>
                        <!-- <div class="message-time">{{ formatTime(form.lastMessageTime) }}</div> -->
                      </div>
                      <!-- <div class="last-message">
                        {{ form.lastMessage || "暂无消息" }}
                      </div> -->
                    </div>
                  </el-col>
                </el-row>
              </el-scrollbar>
            </transition>
          </el-scrollbar>
          <!--新实现-->
          <div v-if="showNewgroup" class="dialog-mask">
            <div class="dialog-wrapper wechat-style">
              <!-- 对话框主体 -->
              <div class="dialog-container">
                <!-- 标题栏 -->
                <div class="dialog-header">
                  <h3 class="title">发起群聊</h3>
                  <div class="close-btn" @click="showNewgroup = false">×</div>
                </div>

                <!-- 内容区（左右分栏） -->
                <div class="dialog-body">
                  <!-- 左侧好友列表 -->
                  <div class="left-panel">
                    <div class="search-box">
                      <input type="text" placeholder="搜索" v-model="searchKey" class="wechat-search">
                    </div>
                    <div class="friend-list">
                      <div v-for="friend in filteredContacts" :key="friend.friendId" class="friend-item"
                        @click="toggleSelection(friend)" @contextmenu.prevent="showContextMenu($event, friend)">
                        <div class="selection-mark" v-show="isSelected(friend)">
                          <div class="check-icon"></div>
                        </div>
                        <img :src="friend.avatar" class="user-avatar">
                        <div class="name">
                          <span>{{ friend.nickname }}</span>
                        </div>
                      </div>
                    </div>
                  </div>


                  <!-- 右侧已选区域 -->
                  <div class="right-panel">
                    <div class="selected-header">
                      <span class="text">已选（{{ selectedFriends.length }}）</span>
                      <button class="clear-btn" @click="clearAll">清空</button>
                    </div>

                    <div class="selected-list">
                      <div v-for="(friend, index) in selectedFriends" :key="index" class="selected-item">
                        <div class="avatar-wrapper">
                          <img :src="friend.avatar" class="user-avatar">
                          <div class="remove-btn" @click.stop="removeFriend(index)">×</div>
                        </div>
                        <span class="name">{{ friend.nickname }}</span>
                      </div>
                    </div>

                    <div class="group-input">
                      <input type="text" v-model="groupName" placeholder="填写群聊名称（必填）" class="wechat-input">
                    </div>
                  </div>
                </div>

                <!-- 底部操作按钮 -->
                <div class="dialog-footer">
                  <button class="wechat-btn cancel" @click="showNewgroup = false">取消</button>
                  <button class="wechat-btn primary" @click="createGroup">完成</button>
                </div>
              </div>
            </div>
          </div>

        </div>

      </template>
    </div>
    <!-- Right side: Chat box -->
    <div class="right-side" v-if="activeTab !== 'chatbot'">
      <!-- Chat header -->
      <div class="chat-header" style="display: flex; align-items: center; justify-content: space-between;">
        <!--        :class="{ 'long-name': currentUser.userName.length > 6 }" -->

        <div v-if="globalUserType === 1 && currentGroup != null">
          <span class="username-wrap">{{ currentGroup.name }}</span>💬
        </div>
        <div v-else-if="currentUser != null">
          <span class="username-wrap">{{ currentUser.nickname }}
          </span>
        </div>

        <!--        <span v-if="currentGroupId" class="username-wrap"
                      :class="{ 'long-name': currentUser.userName.length > 6 }">{{ currentUser.userName }}</span>
           -->
        <el-icon class="header-icon" @click="groupconfig">
          <MoreFilled />
        </el-icon>
      </div>
      <!-- Chat messages -->
      <el-scrollbar class="chat-messages" ref="messageContainer">
        <div v-if="messageType == 0">
          <div class="messageBox" v-for="message in messages" :key="message"
            :class="{ ownMessage: message.sendUserId === loginUser.id, otherMessage: message.sendUserId !== loginUser.id }">
            <div><img :src="message.sendUserId === loginUser.id ? loginUser.avatar : currentUser.avatar" alt=""
                style="border: 1px solid #70c1fa;"></div>

            <div v-if="message.msgType === 'IMAGE'">
              <img :src="message.msg" style="width: 70px; height: 70px; object-fit: cover;">
              <div class="messageTime">{{ message.sendTime }}</div>
            </div>
            <div v-else-if="message.msgType === 'FILE'">
              <a class="file-download-link" :href="message.msg" download
                @click.prevent="handleFileDownload(message.msg)">
                {{ extractFileName(message.msg) }}
              </a>
            </div>
            <div v-else>
              <div class="messageContent">{{ message.msg }}</div>
              <div class="messageTime">{{ message.sendTime }}</div>
            </div>
            <!--          <div v-if=""></div>-->
          </div>
        </div>
        <div v-if="messageType == 1">

          <div class="messageBox" v-for="message in groupMessages" :key="message"
            :class="{ ownMessage: message.sendUserId === loginUser.id, otherMessage: message.sendUserId !== loginUser.id }">
            <!--type=1 成员消息-->
            <div><img
                :src="message.sendUserId === loginUser.id ? loginUser.avatar : currentGroupMember[message.sendUserId]?.avatar"
                alt="avatar" style="border: 1px solid #70c1fa;"></div>

            <div v-if="message.sendUserId !== loginUser.id" class="message-content-wrapper">

              <div class="message-username">
                {{ currentGroupMember[message.sendUserId]?.nickname || '未知用户' }}
              </div>

              <div v-if="message.msgType === 'IMAGE'">
                <img :src="message.msg" style="width: 70px; height: 70px; object-fit: cover;">
                <div class="messageTime">{{ message.sendTime }}</div>
              </div>
              <div v-else-if="message.msgType === 'FILE'">
                <a class="file-download-link" :href="message.msg" download
                  @click.prevent="handleFileDownload(message.msg)">
                  {{ extractFileName(message.msg) }}
                </a>
              </div>
              <div v-else>
                <div class="messageContent">{{ message.msg }}</div>
                <div class="messageTime">{{ message.sendTime }}</div>
              </div>
            </div>

            <div v-else>

              <div v-if="message.msgType === 'IMAGE'">
                <img :src="message.msg" style="width: 70px; height: 70px; object-fit: cover;">
                <div class="messageTime">{{ message.sendTime }}</div>
              </div>
              <div v-else-if="message.msgType === 'FILE'">
                <a class="file-download-link" :href="message.msg" download
                  @click.prevent="handleFileDownload(message.msg)">
                  {{ extractFileName(message.msg) }}
                </a>
              </div>
              <div v-else>
                <div class="messageContent">{{ message.msg }}</div>
                <div class="messageTime">{{ message.sendTime }}</div>
              </div>
            </div>

          </div>
        </div>


      </el-scrollbar>
      <!--      <div class="chat-input">-->
      <!--        <el-input-->
      <!--            v-model="newMessage.content"-->
      <!--            placeholder="请输入聊天内容"-->
      <!--            type="textarea"-->
      <!--            :rows="2"-->
      <!--            resize="none"-->
      <!--            class="message-input"-->
      <!--            @keydown.enter.native.prevent="send"-->
      <!--        ></el-input>-->
      <!--        <el-button-->
      <!--            type="primary"-->
      <!--            @click.native="send"-->
      <!--            class="send-button"-->
      <!--        >发送</el-button>-->
      <!--      </div>-->
      <!-- 工具栏 -->
      <div class="toolbar">
        <div class="left-tools">
          <button class="tool-btn" @click="toggleVoiceMode">
            <span class="icon" :class="isVoiceMode ? 'voice' : 'keyboard'"></span>
          </button>
          <button class="tool-btn" @click="toggleEmojiPicker">
            <span class="icon emoji"></span>
          </button>
          <button class="tool-btn" @click="triggerFileInput">
            <span class="icon file"></span>
            <input type="file" ref="fileInput" class="hidden-file" @change="handleFile">
          </button>

          <button class="tool-btn" @click="triggerLLM">
            <img src="../assets/openai.svg" alt="LLM" width="20">
          </button>
        </div>

        <div class="right-tools">
          <button class="tool-btn" @click="startVideoCall">
            <span class="icon video"></span>
          </button>
          <button class="tool-btn" @click="startVoiceCall">
            <span class="icon phone"></span>
          </button>
        </div>
      </div>

      <!-- 输入区域 -->
      <div class="input-area">
        <!-- 语音输入模式 -->
        <div v-if="isVoiceMode" class="voice-mode">
          <button class="voice-btn" @mousedown="startRecord" @mouseup="stopRecord" @touchstart="startRecord"
            @touchend="stopRecord">
            {{ recording ? `录音中 ${duration}s` : '按住 说话' }}
          </button>
        </div>

        <!-- 文本输入模式 -->
        <div v-else class="text-mode">
          <textarea ref="textarea" v-model="newMessage.msg" class="message-input" placeholder="请输入聊天内容"
            @input="autoResize" @keydown.enter.prevent="send"></textarea>
          <button class="send-btn" @click="send">发送</button>
        </div>
      </div>

      <!-- 表情选择面板 -->
      <div v-show="showEmojiPicker" class="emoji-panel">
        <div class="emoji-item" v-for="emoji in emojis" :key="emoji" @click="insertEmoji(emoji)">{{ emoji }}</div>
      </div>
      <!-- 抽屉内容 -->
      <div class="drawer-mask" v-show="drawerVisible" @click="drawerVisible = false"></div>
      <div class="drawer-container" :class="{ show: drawerVisible }">
        <div class="drawer-content">
          <!-- 群聊内容 -->
          <template v-if="globalUserType === 1">
            <div class="group-title">群聊设置</div>
            <div class="member-list">
              <div v-for="member in groupMembers" :key="member.id" class="member-item">
                <img :src="member.avatar" class="member-avatar" />
                <span>{{ member.nickname }}</span>
              </div>
            </div>
            <div class="action-list">
              <div class="action-item" @click="clearGroupChat">清空聊天记录</div>
              <div class="action-item text-danger" @click="quitGroup">退出群聊</div>
            </div>
          </template>

          <!-- 私聊内容 -->
          <template v-else>
            <div class="user-info">
              <img :src="currentUser.avatar" class="user-avatar" />
              <div class="user-name">{{ currentUser.nickname }}</div>
            </div>
            <div class="action-list">
              <div class="action-item" @click="clearSingleChat">清空聊天记录</div>
              <div class="action-item" @click="deleteFriend">删除好友</div>
              <!-- <div class="action-item text-danger" @click="addBlacklist">加入黑名单</div> -->
            </div>
          </template>
        </div>
      </div>
    </div>
    <div v-else>
      <div class="bot-chat-container">
        <!-- 聊天消息区域 -->
        <div class="bot-chat-messages" ref="messagesContainer">
          <div v-for="message in bot_messages" :key="message.id" :class="['message', message.sender]">
            <div class="avatar">
              <img :src="message.sender === 'user' ? userAvatar : botAvatar" alt="avatar">
            </div>
            <div class="bubble">

              <div class="content" v-html="renderMarkdown(message.content)"></div>
              <!--
                        <div class="content" v-else>{{ message.content }}</div>
              -->
              <div class="status">
                <span class="time">{{ message.timestamp }}</span>
                <span v-if="message.loading" class="typing-indicator">
                  <span class="dot"></span>
                  <span class="dot"></span>
                  <span class="dot"></span>
                </span>
              </div>
            </div>
          </div>
        </div>

        <!-- 输入区域 -->
        <div class="bot-input-area">
          <textarea v-model="inputMessage" @keydown.enter.exact.prevent="sendMessage"
            placeholder="输入你的消息..."></textarea>
          <button @click="sendMessage" :disabled="isSending">
            <span v-if="!isSending">发送</span>
            <span v-else class="sending-indicator"></span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { fetchEventSource } from '@microsoft/fetch-event-source';
import { ArrowDown } from '@element-plus/icons-vue'
// import request from "@/utils/request";
import axios from "axios";
import request from '../utils/request.ts'
import { ElMessageBox, ElMessage } from 'element-plus'
/*智能机器人*/
import { marked } from 'marked'
import DOMPurify from 'dompurify'
import { useRouter } from 'vue-router'

const renderMarkdown = (content) => {
  return DOMPurify.sanitize(marked.parse(content))
}
interface ChatMessage {
  id: string
  content: string
  sender: 'user' | 'bot'
  timestamp: string
  loading?: boolean
}

const bot_messages = reactive<ChatMessage[]>([])
const inputMessage = ref('')
const isSending = ref(false)
const messagesContainer = ref<HTMLElement | null>(null)

const userAvatar = '/path/to/user-avatar.png'
const botAvatar = '/path/to/bot-avatar.png'


const sendMessage = async () => {
  if (!inputMessage.value.trim() || isSending.value) return

  // 用户消息
  const userMsg: ChatMessage = {
    id: Date.now().toString(),
    content: inputMessage.value.trim(),
    sender: 'user',
    timestamp: new Date().toLocaleTimeString()
  }
  bot_messages.push(userMsg)

  // 机器人响应占位
  const botMsg: ChatMessage = {
    id: `bot-${Date.now()}`,
    content: '',
    sender: 'bot',
    timestamp: new Date().toLocaleTimeString(),
    loading: true
  }
  bot_messages.push(botMsg)

  inputMessage.value = ''
  isSending.value = true
  // scrollToBottom()

  try {
    const sessionId = crypto.randomUUID()
    // const eventSource = new EventSource(`api/bot/streamChat?message=${encodeURIComponent(userMsg.content)}`)

    // 发起带有 Authorization 头的流式请求
    await fetchEventSource(`api/streamChat?message=${encodeURIComponent(userMsg.content)}`, {
      method: 'GET',   // 或 POST（需服务端支持）
      headers: {
        'Authorization': sessionStorage.getItem("token"),  // 注入认证头 :ml-citation{ref="8" data="citationList"}
      },
      onopen(response) {
        if (response.ok) return;  // 连接成功
        throw new Error('连接失败');
      },
      onmessage(event) {
        // 处理流式数据（与原 EventSource 逻辑相同）
        const index = bot_messages.findIndex(m => m.id === botMsg.id)
        if (index !== -1) {
          bot_messages[index].content += event.data
          bot_messages[index].loading = false
          bot_messages[index].parsedContent = renderMarkdown(bot_messages[index].content)
          // scrollToBottom()
        }
      },
      onerror(err) {
        console.error('流式请求异常:', err);
      }
    });
    eventSource.onmessage = (event) => {
      const index = bot_messages.findIndex(m => m.id === botMsg.id)
      if (index !== -1) {
        bot_messages[index].content += event.data
        bot_messages[index].loading = false
        bot_messages[index].parsedContent = renderMarkdown(bot_messages[index].content)

        // scrollToBottom()
      }
    }

    eventSource.onerror = () => {
      eventSource.close()
      isSending.value = false
    }

  } catch (error) {
    console.error('Error:', error)
    isSending.value = false
  }
}

let socket = null;
import { reactive, ref, onMounted, getCurrentInstance, nextTick, toRaw, computed } from 'vue'
// 在setup函数中获取组件实例
const instance = getCurrentInstance();
// const container = instance?.proxy?.$refs.messageContainer; // 需添加可选链操作符‌:ml-citation{ref="3,8" data="citationList"}

let messageContainer = ref(null)
let formInline = reactive({
  user: '',
  region: '',
  date: '',
})
import {
  Message,
  User,
  Picture,
  Setting
} from '@element-plus/icons-vue'

let globalUserType = ref(0); //聊天用户类型，0：用户，1：群聊
let activeIndex = ref(-1)//聊天对象默认赋值
let groupMembers = ref([])//当前群成员
let drawerVisible = ref(false) //群设置和单聊设置弹窗
let showNewgroup = ref(false)
let friendApplications = ref([]) //申请好友列表
let applicaionFriends = ref([])
let showDialog = ref(false) // 好友申请控制弹窗显示
let unreadMessage = ref(0)//所有的未读消息数量
let unreadApply = ref(0)//所有的未处理的好友申请数量
let unreadMoment = ref(0)//所有的未看过的朋友圈数量
let showResultLayer = ref(false)
let searchResult = reactive([])
let showUserDialog = ref(false)
let showLoginUserInformation = ref(false)
let showAddFriendForm = ref(false)
let applyReason = ref('')
let isFriend = ref(false)
let filteredContacts = ref([])
let selectedFriends = ref([])//新建群聊选择的好友
let contactSearch = ref('')
let activeTab = ref("message")
let circleUrl = ref('https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png')
let user = ref({})
let isCollapse = ref(false)
let users = ref([])
let chatUser = ref('')
let text = ref("")
let content = ref('')
let currentUser = reactive({})// 当前聊天的人
let currentGroupId = ref("") // 当前聊天的群id
let currentGroup = reactive({}) // 当前聊天的群
let currentGroupMember = reactive({})
let currentSearchUser = reactive({
  avatar: '',
  nickname: '',
  id: '',
  signature: ''
}) // 当前搜索的用户
let loginUser = reactive({
  id: '',
  avatar: '',
  nickname: '',
  signature: '',
  sex: ''

})
let messages = ref([]) //单聊消息
let groupMessages = ref([]) //群聊消息
let messageType = ref(0)// 当前消息类型 0单来哦 1群聊
let messageForm = ref([]) // 聊天所有信息
let curAllMessage = ref([]) // 当前用户聊天所有信息根据消息发送时间倒序排序加上群聊消息
let searchMessageForm = ref([]) // 搜索聊天所有信息
let newMessage = ref({
  id: '',
  revokeId: '',
  chatId: '',
  msgType: '',
  eventType: '',
  code: 200,
  msg: '',
  chatType: 'SINGLE', //聊天对象 private 单聊 group 群聊
  sendUserId: '',
  toUserId: '',
  sendTime: '',
  deviceType: ''
})
let searchUserName = ref('')
let showSearchResult = ref(false)
let groupName = ref('') //群名称
let chatRoomId = ref('')
// 状态管理
const isVoiceMode = ref(false)
const showEmojiPicker = ref(false)
const message = ref('')
const recording = ref(false)
const duration = ref(0)
const fileInput = ref<HTMLInputElement | null>(null)
const textarea = ref<HTMLTextAreaElement | null>(null)
const avatarInput = ref<HTMLInputElement | null>(null)

// 录音相关
let mediaRecorder: MediaRecorder | null = null
let audioChunks: Blob[] = []

// 模拟表情数据
const emojis = [
  '😊', '🥰', '🤩', '😇',   // 笑脸类
  '🤔', '😏', '😒', '🙄',   // 表情符号
  '🤯', '🥶', '😡', '🤢',   // 夸张表情
  '🫡', '🫠', '🥺', '😈',   // 新增Unicode 14-15表情:ml-citation{ref="5" data="citationList"}
  '👋', '🤘', '🤙', '🤌',   // 手势符号
  '🐶', '🐱', '🦁', '🐼',   // 动物类
  '🌻', '🌼', '🍄', '🌵',   // 植物类
  '🍕', '🍔', '🍩', '🍹',   // 食物饮料
  '⚽', '🎮', '🎲', '🎸'    // 活动物品类
];

// 自动调整输入框高度
const autoResize = () => {
  if (textarea.value) {
    textarea.value.style.height = 'auto'
    textarea.value.style.height = `${textarea.value.scrollHeight}px`
  }
}

// 切换语音模式
const toggleVoiceMode = () => {
  isVoiceMode.value = !isVoiceMode.value
  showEmojiPicker.value = false
}

const router = useRouter();
const loginout = () => {
  // 1. 清除所有定时器
  clearInterval(heartbeatInterval);
  clearInterval(tokenCheckInterval);
  window.sessionStorage.clear();
  if (socket) {
    socket.close();
    socket = null;
  }
  router.push('/login').then(() => {
    window.location.reload();
  });
}



let editForm = reactive({
  avatar: '',
  nickname: '',
  signature: '',
  sex: '0'
});
const selectImage = ref<File | null>(null);

const handleInfo = () => {
  editForm.avatar = loginUser.avatar;
  editForm.nickname = loginUser.nickname;
  editForm.signature = loginUser.signature;
  editForm.sex = loginUser.sex;
  activeTab.value = 'info';
}
const submitForm = () => {
  activeTab.value = 'profile';

  const params = new URLSearchParams({
    "nickname": editForm.nickname,
    "signature": editForm.signature,
    "sex": editForm.sex
  });
  request.post("api/chat/user/updateNickname", params).then(res => {
    loginUser.nickname = editForm.nickname;
    loginUser.signature = editForm.signature;
    loginUser.sex = editForm.sex;
  });

  // console.log("selectImage:", selectImage);
  if (selectImage.value != null) {
    console.log("更新头像");
    const formData = new FormData();
    formData.append('image', selectImage.value);
    // formData.append('model', 'user_icon');
    request.post("api/chat/user/updateAvatar", formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }).then(res => {
      loginUser.avatar = res.data.data;
    });
  }

}

const triggerAvatarInput = () => {
  avatarInput.value?.click()
}

const handleAvatarChange = (e: Event) => {
  const input = e.target as HTMLInputElement
  const file = input.files?.[0]
  if (!file) return;
  console.log("file:", file);
  // 验证文件类型和大小
  if (!file.type.match('image.*')) {
    alert('请选择图片文件');
    return;
  }

  if (file.size > 2 * 1024 * 1024) {
    alert('图片大小不能超过2MB');
    return;
  }
  selectImage.value = file;
  const blobUrl = URL.createObjectURL(file);
  editForm.avatar = blobUrl;

}
// 文件处理
const triggerFileInput = () => {
  fileInput.value?.click()
}
const handleFile = (e: Event) => {
  const input = e.target as HTMLInputElement
  const file = input.files?.[0]
  if (file) {
    console.log('选择文件:', file);
    console.log("文件类型:", file.type);

    let type = '';
    if (file.type.startsWith("image/")) {
      type = "IMAGE";
    } else {
      type = "FILE";
    }
    console.log("type:", type);
    const formData = new FormData();
    formData.append('file', file); // 键名必须与后端 @RequestParam("file") 一致
    formData.append('model', 'user'); // 替换为实际模块名
    request.post("api/sys/upload/file", formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }).then(res => {

      console.log(res.data.data);
      const params = {
        msgType: type,
        url: res.data.data.url
      }
      sendFile(params);
    });

  }
}

const quitGroup = () => {
  console.log("正在退出群聊");
  const params = new URLSearchParams({
    groupId: currentGroupId.value
  });
  request.post("api/group/regulate/logoutGroup", params).then(res => {
    if (res.data.data === true) {
      console.log("退出群聊成功!");
    } else {
      console.log("退出群聊失败!");
    }
  });
}

const extractFileName = (url) => {
  return url.split('/').pop() || '未命名文件';
};

// 处理文件下载（可添加额外逻辑，如权限检查）
const handleFileDownload = (fileUrl) => {
  // 方式1：直接通过 <a download> 触发下载（简单场景）
  // 无需额外代码，浏览器会自动处理

  // 方式2：如果需要额外逻辑（如验证登录）
  const link = document.createElement('a');
  link.href = fileUrl;
  link.download = extractFileName(fileUrl);
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);

  // 可选：下载成功提示
  ElMessage.success('文件下载中...');
};

// 表情处理
const toggleEmojiPicker = () => {
  showEmojiPicker.value = !showEmojiPicker.value
}

const insertEmoji = (emoji: string) => {
  console.log(emoji)
  console.log(newMessage.value.msg)
  if (newMessage.value.msg) {
    newMessage.value.msg += emoji
  } else {
    newMessage.value.msg = emoji
  }
  console.log("加入表情包之前")
  console.log(emoji)
  console.log(newMessage.value.msg)
  nextTick(autoResize)
  showEmojiPicker.value = false
}

// 语音录制
const startRecord = async () => {
  try {
    const stream = await navigator.mediaDevices.getUserMedia({ audio: true })
    mediaRecorder = new MediaRecorder(stream)

    mediaRecorder.ondataavailable = (e) => {
      audioChunks.push(e.data)
    }

    mediaRecorder.onstop = () => {
      const audioBlob = new Blob(audioChunks, { type: 'audio/webm' })
      console.log('录音文件:', audioBlob)
      const formData = new FormData()
      formData.append('audioFile', audioBlob, 'recording.webm')  // 使用FormData封装二进制数据:ml-citation{ref="2" data="citationList"}
      request.post('/api/audio/upload', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      }).then(res => {
        console.log('上传成功:', res.data)
      })
      //录音文件清空
      audioChunks = []
    }

    mediaRecorder.start()
    recording.value = true
    startTimer()
  } catch (err) {
    console.error('录音权限被拒绝:', err)
  }
}

const stopRecord = () => {
  mediaRecorder?.stop()
  recording.value = false
  duration.value = 0
}

// 录音计时器
const startTimer = () => {
  const timer = setInterval(() => {
    if (recording.value) {
      duration.value++
    } else {
      clearInterval(timer)
    }
  }, 1000)
}

function clearSingleChat() {
  const params = new URLSearchParams({
    userId: loginUser.id,
    chatId: chatRoomId.value
  });
  request.post("api/chat/record/cleanMsg", params).then(res => {
    console.log("清空消息成功！");
  });
}
// 通话功能
const startVideoCall = () => console.log('发起视频通话')
// const activeTab = ref('message') // 当前激活的tab
const totalUnread = ref(0)       // 未读消息数示例

// 初始化虚拟数据
const initMockData = () => {
  messageForm.value = Array.from({ length: 15 }, (_, i) => ({
    receiveUser: {
      id: `user_${i + 1}`,
      userName: `用户 ${i + 1}`,
      avatar: `https://picsum.photos/40/40?random=${i}`,
      isOnline: Math.random() > 0.5
    },
    noReadMessageLength: Math.floor(Math.random() * 5),
    lastMessage: generateMockMessage(),
    lastMessageTime: Date.now() - Math.random() * 86400000
  }))
}

//显示用户卡片
const showUserCard = (user) => {
  activeTab.value = 'profile'
}
// 显示成员卡片
const showMemberCard = (user) => {
  currentSearchUser = user;
  showResultLayer.value = false;
  showUserDialog.value = true;
  isFriend.value = false;

}
// 搜索用户
const handleSearchUser = () => {
  if (!contactSearch.value.trim()) return;

  try {
    // 模拟API调用
    const res = request.get("api/chat/user/getSearchList", {
      params: { "keyword": contactSearch.value }
    }
    ).then(res => {
      if (res.data.code === 200) {
        searchResult = res.data.data;
        showResultLayer.value = true;
        console.log(searchResult);
      } else {
      }
      console.log(res)
    })
  } catch (error) {
    searchResult = null;
    showResultLayer.value = true;
  }
}
//展示所有的用户申请列表
const showNewFriendList = () => {
  getAllFriendRequests()
  //新好友列表弹窗
  showDialog.value = true

}
//关闭查看所有好友申请弹窗
const closeApply = () => {
  showDialog.value = false
}
// 关闭申请弹窗
const closeDialog = () => {
  showUserDialog.value = false;
  currentSearchUser = {};
  activeTab.value = '';
}

// 提交好友申请
const handleSubmitApply = () => {
  try {
    request.post('/api/friend/apply/add', {
      friendId: currentSearchUser.id,
      applyReason: applyReason.value,
      source: "搜索添加"
    }).then(res => {
      console.log(res)
    });
    showUserDialog.value = false
    ElMessage.success('好友申请已发送');
    showAddFriendForm.value = false;
  } catch (error) {
    ElMessage.error('发送失败，请重试');

  }
}

// 进入好友申请表单
const enterAddFriend = (currentSearchUser) => {
  showAddFriendForm.value = true;
  applyReason.value = '';
}
const generateMockMessage = () => {
  const messages = [
    '你好，今天有空吗？',
    '项目文档已更新',
    '[图片]',
    '[文件]',
    '明天会议时间确认？'
  ]
  return messages[Math.floor(Math.random() * messages.length)]
}
const updateTab = (tab) => {
  activeTab.value = tab
  console.log(activeTab.value)
  if (activeTab.value === "contact") {
    searchAllFriends()
  } else if (activeTab.value === "message") {
    searchUserMessage()
  }
}
//搜索当前用户所有信息 请求后端完成则更新所有用户信息保存到前端数据 拿到所有信息 from_user:发送者 send_user:接受者 create_time 发送消息时间 is_read 是否已读 message_id 消息id message_content 消息内容
const searchAllFriends = () => {
  request.get("api/friend/regulate/list").then(res => {

    if (res.data.success && res.data.code === 200) {
      filteredContacts.value = res.data.data;
    } else {
      console.error("获取联系人失败:", res.data.message);
      router.push('/login');
    }
  }).catch(error => {
    console.error("请求失败:", error);
    router.push('/login');

  });
}
const formatTime = (timestamp) => {
  const date = new Date(timestamp)
  const hours = date.getHours().toString().padStart(2, '0')
  const minutes = date.getMinutes().toString().padStart(2, '0')
  return `${hours}:${minutes}`
}

const triggerLLM = () => {
  if (globalUserType.value === 0) {
    if (loginUser.id === '' || loginUser.id === null) {
      ElMessage.warning('登录状态无效，请重新登录')
      return
    }
    if (Object.keys(currentUser).length === 0) {
      ElMessage.error('未选择用户!')
      return
    }
    if (loginUser.id === currentUser.id) {
      ElMessage.error('不能给自己发送信息!')
      return
    }

    const arr = [];
    messages.value.forEach(message => {
      if (message.msgType === "TEXT") {
        const tmp = {
          "content": message.msg,
          "role": "user",
          "name": currentUser.nickname
        }
        if (loginUser.id === message.sendUserId) {
          tmp.name = "I";
        }

        arr.push(tmp);
      }
    });

    const params = {
      messages: [{
        "content": "以下是群聊中的消息，根据这些信息你直接给出回答",
        "role": "system",
        "name": "background"
      }]
    }
    const lastMessages = arr.slice(-4);
    params.messages = params.messages.concat(lastMessages);
    request.post("api/deepseek/chat/completions", params, {
      headers: {
        'Content-Type': 'application/json'
      }
    }).then(res => {
      console.log("res:", res);
      newMessage.value.msg = res.data.data;
    });
  } else {

    const arr = [];
    groupMessages.value.forEach(message => {
      if (message.msgType === "TEXT") {
        const tmp = {
          "content": message.msg,
          "role": "user",
          "name": currentGroupMember[message.sendUserId]?.nickname
        }
        if (loginUser.id === message.sendUserId) {
          tmp.name = "I";
        }
        arr.push(tmp);
      }
    });
    // console.log("arr:", arr);
    const params = {
      messages: [{
        "content": "以下是群聊中的消息，根据这些信息你直接给出回答",
        "role": "system",
        "name": "background"
      }]
    }
    params.messages = params.messages.concat(arr.splice(-4));
    // console.log("params:", params);
    request.post("api/deepseek/chat/completions", params, {
      headers: {
        'Content-Type': 'application/json'
      }
    }).then(res => {
      console.log("res:", res);
      newMessage.value.msg = res.data.data;
    });
  }
}
const sendFile = (msg) => {
  if (globalUserType.value === 0) {
    if (loginUser.id === '' || loginUser.id === null) {
      ElMessage.warning('登录状态无效，请重新登录')
      return
    }
    if (Object.keys(currentUser).length === 0) {
      ElMessage.error('未选择用户!')
      return
    }
    if (loginUser.id === currentUser.id) {
      ElMessage.error('不能给自己发送信息!')
      return
    }

    newMessage.value.sendUserId = loginUser.id;
    newMessage.value.toUserId = currentUser.id;
    newMessage.value.chatType = "SINGLE";
    newMessage.value.chatId = chatRoomId;
    newMessage.value.msgType = msg.msgType;
    newMessage.value.msg = msg.url;
    if (typeof (WebSocket) == "undefined") {
      console.log("您的浏览器不支持WebSocket");
    } else {
      let socketUrl = "ws?token=" + window.sessionStorage.getItem("token");
      if (!socket) {
        socket = new WebSocket(socketUrl);
      }
      socket.send(JSON.stringify(newMessage.value));

      //打开事件
      // socket.onopen = () => {
      //   socket.send(JSON.stringify(newMessage.value));
      // }
      // socket.onmessage = (event) => {
      //   newMessage.value.msg = '';
      //   const params = {
      //     "id": currentUser.id,
      //     "chatId": chatRoomId
      //   }
      //   chooseUser(params);
      // }
      // socket.onerror = (error) => {
      //   console.error("WebSocket错误:", error);
      // };

    }
  } else {

    newMessage.value.message = newMessage.value.msg.trim()
    if (loginUser.id == null) {
      ElMessage.error('登录用户编号获取失败,请重新登录!')
      return
    }
    if (currentGroupId.value === null || currentGroupId.value === "") {
      ElMessage.error("未选择群聊")
      return
    }
    newMessage.value.sendUserId = loginUser.id;
    newMessage.value.chatType = "GROUP";
    newMessage.value.toUserId = currentGroupId.value;
    newMessage.value.chatId = chatRoomId;
    newMessage.value.msgType = msg.msgType;
    newMessage.value.msg = msg.url;
    if (typeof (WebSocket) == "undefined") {
      console.log("您的浏览器不支持WebSocket");
    } else {
      socket.send(JSON.stringify(newMessage.value));


    }
  }
}


const send = () => {
  if (globalUserType.value === 0) {
    if (!newMessage.value.msg.trim()) {
      ElMessage.warning('请输入聊天内容')
      return
    }
    newMessage.value.msg = newMessage.value.msg.trim()
    if (loginUser.id == null) {
      ElMessage.warning('登录状态无效，请重新登录')
      return
    }
    if (loginUser.id === currentUser.id) {
      ElMessage.error('不能给自己发送信息!')
      return
    }
    newMessage.value.sendUserId = loginUser.id;
    newMessage.value.toUserId = currentUser.id;
    newMessage.value.chatType = "SINGLE";
    newMessage.value.chatId = chatRoomId;
    newMessage.value.msgType = "TEXT";
    if (typeof (WebSocket) == "undefined") {
      console.log("您的浏览器不支持WebSocket");
    } else {
      console.log("您的浏览器支持WebSocket");
      // console.log(newMessage.value.msg);
      socket.send(JSON.stringify(newMessage.value));

    }
  } else {
    // console.log("发送群聊id", currentGroupId.value);
    if (!newMessage.value.msg.trim()) {
      ElMessage.warning('请输入聊天内容')
      return
    }
    newMessage.value.message = newMessage.value.msg.trim()
    if (loginUser.id == null) {
      ElMessage.error('登录用户编号获取失败,请重新登录!')
      return
    }
    newMessage.value.sendUserId = loginUser.id;
    // newMessage.value.groupId = currentGroupId.value
    newMessage.value.chatType = "GROUP";
    newMessage.value.toUserId = currentGroupId.value;
    newMessage.value.chatId = chatRoomId;
    newMessage.value.msgType = "TEXT";
    if (typeof (WebSocket) == "undefined") {
      console.log("您的浏览器不支持WebSocket");
    } else {
      console.log("您的浏览器支持WebSocket");
      socket.send(JSON.stringify(newMessage.value));

    }
  }
  //清空聊天框
  newMessage.value.msg = '';

}
//当前群聊设置
const groupconfig = () => {
  drawerVisible.value = true;
  // request.get("api/group/getGroupMember", {
  //   params: {
  //     "groupId": currentGroupId.value,
  //   }
  // }).then(res => {
  //   console.log(res)
  //   groupMembers.value = res.data.data
  //   console.log(groupMembers.value)
  // })
}
//搜索当前用户所有信息 请求后端完成则更新所有用户信息保存到前端数据 拿到所有信息 from_user:发送者 send_user:接受者 create_time 发送消息时间 is_read 是否已读 message_id 消息id message_content 消息内容
const searchUserForForm = () => {
  console.log(searchUserName.value);
  const keyword = searchUserName.value?.trim() || "";

  // 2. 如果关键词为空，可提前返回或提示用户
  if (!keyword) {
    console.warn("搜索关键词不能为空");
    // this.$message.warning("请输入搜索内容"); // 若使用ElementUI可加提示
    return;
  }
  request.get("api/chat/user/getFriendList", {
    params: { keyword }
  }).then(res => {
    console.log(res)
    showSearchResult.value = true
    searchMessageForm.value = res.data.data;
    console.log(searchMessageForm.value)
  })
}
//搜索当前用户发过的消息和接受到的消息，根据最后一条消息时间倒序排序
const searchUserMessage = () => {
  request.get("api/chat/record/listAll").then(res => {
    curAllMessage.value = res.data.data;
  })

}

const handleSearchInput = (val) => {
  if (!val) {
    showSearchResult.value = false
  }
}
const handleSelectUser = (user) => {
  console.log("handleSelectUser");
  const message = {
    chatType: user.chatType === "GROUP" ? 1 : 0,
    user: user
  }
  chooseUser(message)
  showSearchResult.value = false
  showUserDialog.value = false
  searchUserName.value = ''
  activeTab.value = 'message'
}

const handleItemClick = (form, index) => {
  activeIndex = index;
  chooseUser(form);

}
const getUserInfo = async (userId) => {

  request.get("api/user/regulate/userInfo", { params: { userId } }).then(res => {
    Object.assign(currentUser, res.data.data);
  });

}
const getGroupInfo = async (groupId) => {

  const res = await request.get("api/group/regulate/groupInfo", {
    params: {
      "groupIds": groupId
    }
  });
  currentGroup = res.data.data[0];
  // console.log("currentGroup:", currentGroup);
  const members = await request.get("api/group/regulate/member/list", {
    params: {
      "groupId": groupId
    }
  });
  groupMembers.value = members.data.data;
  members.data.data.forEach(member => {
    // currentGroupMember.set(member.userId, member);
    currentGroupMember[member.userId] = member;
  });

}
const chooseUser = (msg) => {
  const type = msg.chatType === "GROUP" ? 1 : 0;
  chatRoomId = msg.chatId;
  if (type === 0) {
    globalUserType.value = 0;
    getUserInfo(msg.id);
    messageType.value = 0;//设置当前类型为单聊消息 用于区分右侧消息内容
    fetchMessages(msg);
  } else if (type === 1) {
    messageType.value = 1; //设置当前类型为单聊消息 用于区分右侧消息内容
    currentGroupId.value = msg.id;
    getGroupInfo(msg.id);
    fetchMessagesGroup(chatRoomId);
    globalUserType.value = 1;

  }

}
//更新消息列表单聊
const fetchMessages = (msg) => {
  // console.log("msg " + msg);
  const params = {
    chatId: msg.chatId,
    pageFlippingType: "PULL_UP"
  }
  // console.log("chatId " + msg.chatId);
  request.post("api/chat/record/listSingleChat", params, {
    headers: {
      'Content-Type': 'application/json'
    }
  }).then(res => {
    messages.value = res.data.data
    // 将聊天记录总下拉到最下方
    nextTick(() => {
      scrollToBottom()
    })
  })
}
//更新消息列表群聊
const fetchMessagesGroup = (chatId) => {
  request.get("api/chat/record/listGroupChat", {
    params: {
      "chatId": chatId
    }
  }).then(res => {

    groupMessages.value = res.data.data;

    // 将聊天记录总下拉到最下方
    nextTick(() => {
      scrollToBottom()
    })
  })
}
//获取所有的好友请求
const getAllFriendRequests = () => {
  request.get("api/friend/apply/list").then(res => {

    friendApplications.value = [];
    applicaionFriends.value = [];
    res.data.data.forEach(friendApply => {
      if (friendApply.status === 0) {
        if (friendApply.friendId === loginUser.id) {
          friendApplications.value.push(friendApply);
        } else {
          applicaionFriends.value.push(friendApply);
        }
      }

    })
    unreadApply.value = applicaionFriends.value.length + friendApplications.value.length;
  });
}



/*同意和拒绝好友*/
const handleApply = async (item, accept) => {
  const params = {
    applyId: item.id,
    nickname: item.nickname
  }
  if (accept) {
    await request.post("api/friend/apply/agree", params, {
      headers: {
        'Content-Type': 'application/json'
      }
    }).then(res => {
      item.status = 1;
    })
  } else {
    await request.post("api/friend/apply/reject", params, {
      headers: {
        'Content-Type': 'application/json'
      }
    }).then(res => {
      item.status = 2;
    })
  }

}
/*发起群聊*/
const newChatGroup = () => {
  searchAllFriends()
  showNewgroup.value = true
  //更新最后消息列表
  // searchUserMessage()
  // console.log("333")
  // console.log(filteredContacts.value)
  // console.log(filteredContacts.value)

}

// 判断是否已选中
const isSelected = (friend) => {

  return selectedFriends.value.some(f =>
    f.friendId === friend.friendId
  );
}

// 切换选择状态
const toggleSelection = (friend) => {
  const index = selectedFriends.value.findIndex(f =>
    f.friendId === friend.friendId
  );
  console.log(index, friend)
  if (index > -1) {
    selectedFriends.value.splice(index, 1);
  } else {
    selectedFriends.value.push(friend);
  }
}
// 移除单个好友
const removeFriend = (index) => {
  selectedFriends.value.splice(index, 1);
}

// 清空所有选择
const clearAll = () => {
  selectedFriends.value = [];
}

// 创建群组
const createGroup = () => {
  if (!groupName.value) {
    alert('请填写群聊名称');
    return;
  }
  if (selectedFriends.value.length < 1) {
    alert('请至少选择一位成员');
    return;
  }

  // 1. 准备成员ID数组（假设是 [1, 2, 3]）
  const members = selectedFriends.value.map(f => f.friendId);

  // 2. 构建 URLSearchParams（确保每个成员ID作为独立的键值对）
  const params = new URLSearchParams();
  members.forEach(id => {
    params.append('members', id.toString()); // 关键点：同名参数多次追加
  });

  // 3. 发送请求（Content-Type 默认为 application/x-www-form-urlencoded）
  request.post("api/group/regulate/create", params).then(res => {
    console.log("群组创建成功:", res);
    const idStr = res.data.data.split('_')[1];
    // const id = parseInt(idStr, 10);
    const params1 = new URLSearchParams({
      groupId: idStr,
      name: groupName.value
    });
    request.post("api/group/regulate/updateGroupName", params1).then(res => {
      if (res.data.data === true) {
        console.log("更新群聊名称成功！");
      } else {
        console.log("更新群聊名称失败！");
      }
    }).then(aft => {
      groupName.value = '';
      searchUserMessage();   //更新消息列表
    })

  }).catch(error => {
    console.error("请求失败:", error);
  });

  clearAll();

  //关闭弹窗
  showNewgroup.value = false;
}
// 消息过多的时候滚动到最新消息位置
const scrollToBottom = () => {
  // 使用 $refs 来获取对消息容器的引用
  const container = instance?.proxy?.$refs.messageContainer
  console.log(container)
  var assign = Object.assign({}, container);
  console.log(assign);
  console.log(assign.wrapRef);
  assign.wrapRef.scrollTop = 100000
  // console.log(container.scrollHeight)
  // messageContainer.value.wrapRef.scrollTop = messageContainer.value.wrapRef.scrollHeight;
  //
  // .$refs.wrap
  // 滚动到底部
  // container.scrollTop = 900
}

//单向删除好友
const deleteFriend = async () => {
  console.log(currentUser)
  await ElMessageBox.confirm('确定删除该好友？', '警告', { type: 'warning' })

  request.post(`api/friends/delete?friendId=${currentUser.id}`).then(
    res => {
      console.log(res)
      loginUser = res.data.data
      console.log(loginUser)
    }
  )
}

//单向拉黑好友
const addBlacklist = () => {
  request.post("api/friends/black", {
    "friendId": currentUser.friendId
  }).then(
    res => {
      console.log(res)
      loginUser = res.data.data
      console.log(loginUser)
    }
  )
}
const beforeCreate = async () => {
  axios.defaults.headers.common['Authorization'] = window.sessionStorage.getItem("token");
  const res = await request.get("api/user/regulate/info");
  window.sessionStorage.setItem("user", res.data.data);
  Object.assign(loginUser, res.data.data);
  console.log("loginUser:", loginUser);
}

const selectContact = (user) => {
  const msg = {
    chatType: "SINGLE",
    id: user.friendId,
    chatId: ""
  }

  request.get("api/chat/room/gotoSendMsg", {
    params: {
      friendId: user.friendId
    }
  }).then(res => {
    console.log(res.data.data);
    msg.chatId = res.data.data;
    chooseUser(msg);
  });


}

let lastMessageTime = null;
let heartbeatInterval; // 心跳定时器
let tokenCheckInterval; // token 检查定时器
function connect() {
  // 1. 如果已有连接，先关闭
  if (socket) socket.close();

  // 2. 建立新连接
  socket = new WebSocket(`/ws?token=${sessionStorage.getItem("token")}`);
  // 3. 连接成功时启动心跳
  socket.onopen = () => {
    console.log("WebSocket已连接");
    const msg = {
      id: '',
      revokeId: '',
      chatId: '',
      msgType: "HEART_BEAT",
      eventType: '',
      code: '',
      msg: '',
      chatType: '',
      sendUserId: '',
      toUserId: '',
      sendTime: '',
      deviceType: ''
    }

    heartbeatInterval = setInterval(() => {
      socket.send(JSON.stringify(msg));
    }, 50000); // 每50秒一次心跳

    tokenCheckInterval = setInterval(() => {
      const now = Date.now();
      if (lastMessageTime && now - lastMessageTime < 170 * 60 * 1000) {
        refreshToken();
      } else {
        loginout();
      }
    }, 170 * 60 * 1000);
  };

  socket.onmessage = (event) => {
    const serverData = JSON.parse(event.data); // 解析服务端发送的JSON数据
    console.log("收到服务端消息:", serverData);
    if (serverData.sendUserId !== loginUser.id) {
      totalUnread.value += 1;
    }
    chooseUser(serverData);
    lastMessageTime = Date.now();
  }
  // 4. 断开时自动重连
  socket.onclose = () => {
    setTimeout(connect, 30000); // 30秒后重连
  };

  // 5. 错误处理
  socket.onerror = (err) => {
    console.error("WebSocket错误:", err);
    socket.close(); // 触发onclose自动重连
    socket = null;
  };
}

function refreshToken() {
  request.post("api/user/regulate/refreshToken").then(res => {
    window.sessionStorage.setItem("token", res.data.data.token);
  })
}
onMounted(() => {
  beforeCreate()
  initMockData()
  searchAllFriends()   // 搜索所有好友
  connect()            // websocket 连接
  searchUserMessage()  //默认进入网页当前为消息页
  getAllFriendRequests() //获取好友申请请求
  scrollToBottom()
});


</script>
<style scoped>
.left-side {
  max-width: 370px;
  position: relative;
  /* Position relative for absolute positioning */
  flex: 1;
  height: 100vh;
  /*
  padding: 20px;
  */
  border-right: 1px solid #eaeaea;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.search-input {
  flex: 1;
  max-width: 370px;
  padding: 8px 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 14px;
  transition: border-color 0.3s;
}

.search-input:focus {
  outline: none;
  border-color: #409EFF;
  box-shadow: 0 0 4px rgba(64, 158, 255, 0.3);
}

.contact-search-input {
  flex: 1;
  max-width: 370px;
  padding: 8px 12px;
  font-size: 14px;
  transition: border-color 0.3s;
}

.contact-search-input:focus {
  outline: none;
  border-color: #409EFF;
  box-shadow: 0 0 4px rgba(64, 158, 255, 0.3);
}

.add-btn {
  width: 32px;
  height: 32px;
  border: none;
  background: none;
  color: #909399;
  font-size: 20px;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 移除点击态边框 */
.add-btn:focus,
.add-btn:active {
  border: none !important;
  outline: none !important;
  box-shadow: none !important;
  /* 双重保障 ‌:ml-citation{ref="4,7" data="citationList"} */
}

/* 按钮悬停动画 */
.add-btn:hover {
  transform: scale(1.1);
  color: #409EFF;
}

/*!* 移动端适配 *!
@media (max-width: 768px) {
  .search-wrapper {
    padding: 8px;
  }

  .search-input {
    max-width: none;
  }
}*/
/* 外层容器 - 禁用滚动并启用弹性布局 */
.user-list-scroll {
  height: 100vh !important;
  /* 强制视口高度 */
  min-height: 0;
  /* 允许内容压缩 */
  display: flex;
  flex-direction: column;
  overflow: hidden !important;
  /* 禁用外部滚动 */

  /* Element 滚动容器修正 */
  .el-scrollbar {
    flex: 1;
    /* 占据剩余空间 */
    min-height: 0;
    /* 关键! 解除高度限制 */

    /* 滚动包装层 */
    .el-scrollbar__wrap {
      height: calc(100% + 36px) !important;
      /* 滚动补偿 */
      max-height: none !important;
      overflow-y: scroll !important;
      padding-bottom: 36px !important;
      /* 滚动条安全区 */
      scroll-behavior: smooth;
    }

    /* 内容视图层 */
    .el-scrollbar__view {
      min-height: calc(100% + 10px);

      /* 强制溢出触发滚动 */
      >div {
        padding-bottom: 8px;
        /* 内容补偿 */
      }
    }
  }

  /* 搜索结果悬浮层特定修正 */
  .user-list-scroll-search {
    .el-scrollbar__wrap {
      padding-bottom: 24px !important;
      /* 增加补偿 */
    }
  }
}

/* 浏览器兼容方案 */
@supports (-moz-appearance:none) {
  .el-scrollbar__wrap {
    scrollbar-width: thin;
    padding-bottom: 18px !important;
    /* 火狐滚动条较宽 */
  }
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
}

/*.search-active + .user-list-scroll {
  filter: blur(1px);
  pointer-events: none;
}*/
/*.user-list-scroll-search{
  height: calc(100% - 40px);
  overflow-y: auto;
}*/
.user-avatar-wrapper {
  position: relative;
  display: inline-block;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
  border: 1px solid #74ffd2;
}

/*.user-name {
  font-weight: 800;
  white-space: nowrap; !* 不换行 *!
  overflow: hidden; !* 溢出隐藏 *!
  padding-left: 15px;
  text-overflow: ellipsis; !* 超出显示省略号 *!
  text-align: left; !* 添加左对齐属性 *!
}*/

.user-last-message {
  color: #a19f9f;
  font-size: 14px;
  white-space: nowrap;
  overflow: hidden;
  padding-left: 15px;
  text-overflow: ellipsis;
  text-align: left;
  /* 添加左对齐属性 */
}



/*.chat-header {
  padding: 20px;
  border-bottom: 1px solid #eaeaea;
  font-size: 1.2em;
  color: #37474F;
}*/

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
}

.chat-input {
  padding: 20px;
  display: flex;
  align-items: center;
}

.message-input {
  flex: 1;
  margin-right: 10px;
}

.send-button {
  flex-shrink: 0;
}

.user-item {
  display: flex;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #f0f0f2;
}

.user-item:hover {
  background-color: #E0E0E0;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.user-details {
  flex-grow: 1;
  /* 填充剩余空间 */
}

/* 头部行布局 - 微信样式 */
.header-line {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
  width: 100%;
}

.user-name {
  font-size: 16px;
  color: #191919;
  max-width: 70%;
  white-space: nowrap;
}

.message-time {
  font-size: 12px;
  color: #888;
  flex-shrink: 0;
  margin-left: 8px;
}

/* 消息预览样式 */
.last-message {
  font-size: 11px;
  color: #888;
  text-align: left;
  width: 100%;
  line-height: 1.4;
}

/* 通用截断样式 */
.ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 适配深色模式 */
@media (prefers-color-scheme: dark) {
  .user-name {
    color: #e5e5e5;
  }

  .last-message {
    color: #999;
  }

  .user-details {
    border-color: #2d2d2d;
  }
}

.messageBox {
  display: flex;
  align-items: flex-start;
  /* 将头像和文本第一行对齐 */
  margin-bottom: 10px;
}

.messageBox img {
  width: 40px;
  /* 调整头像大小 */
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
  margin-left: 10px;
}

.messageContent {
  max-width: 60%;

  /* 调整发送信息宽度 */
  padding: 10px;
  border-radius: 8px;
  background-color: #f0f0f0;
  text-align: left;
  /* 文本左对齐 */
  word-wrap: break-word;
  /* 当文本过长时自动换行 */
}

.messageTime {
  font-size: 12px;
  color: #999;
  margin-left: 10px;
  margin-top: 5px;
  /* 将发送时间与文本分隔开 */
}

.ownMessage {
  flex-direction: row-reverse;
  align-items: flex-end;
  /* 将发送时间放置在最下方的贴右位置 */
}

.otherMessage {
  flex-direction: row;
  align-items: flex-end;
  /* 将发送时间放置在最下方的贴左位置 */
}

.online-dot {
  position: absolute;
  top: 0;
  left: 0;
  z-index: 1;
  width: 10px;
  height: 10px;
  background-color: #01c201;
  border-radius: 50%;
}

.message-badge .el-badge__content {
  position: absolute;
  bottom: 5px;
  /* Adjust to your desired position */
  left: 5px;
  /* Adjust to your desired position */
  background-color: #f56c6c;
  /* Red background for visibility */
  color: white;
  /* White text color */
}

.el-scrollbar {
  height: 100vh;
  overflow: hidden;

  /* 关键容器修正 */
  .el-scrollbar__wrap {
    max-height: 100vh !important;
    /* 解除默认高度限制 */
    padding-bottom: 8px !important;
    /* 滚动条安全区 */
    overflow-y: scroll !important;
    /* 强制启用滚动 */
  }

  .el-scrollbar__view {
    min-height: calc(100% + 1px);
    /* 强制触发溢出滚动 */
  }
}

/* 容器布局 */
.chat-container {
  display: flex;
  height: 100vh;
  /* 使用视口高度而非百分比 */
  width: 1280px;
  /* 固定宽度 */
  overflow: hidden;
  /* 隐藏滚动条 */

  /* 防止父容器溢出 */
  flex-shrink: 0;
  box-sizing: border-box;

  /*!* 弹性子元素布局 *!
  > * {
    flex: 1;
    min-width: 0; !* 防止弹性元素溢出 *!
  }*/
}

/* 响应式处理 */
@media screen and (max-width: 1280px) {
  .chat-container {
    width: 100vw;
    /* 小屏幕时占满视口 */
    transform: translateX(0);
    /* 防止横向滚动 */
  }
}


/*!* 左侧用户列表 *!
.left-side {
  width: 280px;
  border-right: 1px solid #e5e5e5;
  display: flex;
  flex-direction: column;
}*/

.search-wrapper {
  position: absolute;
  padding: 10px;
  border-bottom: 1px solid #e5e5e5;
  display: flex;
  gap: 8px;
  /* 元素间距 */
}

/*.user-list-scroll {
  flex: 1;
  overflow: hidden;
}*/
/*
 //min-width: 400px;
 */
/* 右侧聊天区域 */
.right-side {
  flex: 1;
  display: flex;
  flex-direction: column;
  width: 1330px;
}

/* 聊天头部 */
.chat-header {
  padding: 16px 24px;
  border-bottom: 1px solid #e5e5e5;
  background: #fafafa;
  font-size: 16px;
  font-weight: 500;
  line-height: 1.5;
}

/* 消息区域 */
.chat-messages {
  flex: 1;
  padding: 20px 24px;
  background: #f5f5f7;
  overflow-y: auto;
}

/* 消息气泡 */
.messageBox {
  display: flex;
  margin-bottom: 20px;
  max-width: 70%;
}

.ownMessage {
  margin-left: auto;
  flex-direction: row-reverse;
}

.otherMessage {
  margin-right: auto;
}

/* 头像样式 */
.avatar-wrapper {
  flex-shrink: 0;
  margin: 0 12px;
}

.message-avatar {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  border: 2px solid #70c1fa;
}

/* 消息内容 */
.message-content-wrapper {
  max-width: calc(100% - 64px);
  position: relative;
}

.ownMessage .message-content-wrapper {
  align-items: flex-end;
}

.messageContent {
  padding: 12px 16px;
  border-radius: 12px;
  line-height: 1.5;
  word-break: break-word;
  background: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);

}

.ownMessage .messageContent {
  background: #95ec69;
  border-radius: 12px 12px 0 12px;
  margin-left: auto;
  /* 关键属性：右对齐 */
  text-align: left;
}

.otherMessage .messageContent {
  background: #ffffff;
  border-radius: 12px 12px 12px 0;
  margin-right: auto;

}

/* 消息时间 */
.messageTime {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

.ownMessage .messageTime {
  text-align: right;
}

/* 输入区域 */
.chat-input {
  padding: 16px 24px;
  border-top: 1px solid #e5e5e5;
  background: #fff;
  display: flex;
  gap: 12px;
}

.message-input {
  flex: 1;
}

.message-input>>>.el-textarea__inner {
  padding: 8px 12px;
  border-radius: 8px;
  line-height: 1.5;
}

.send-button {
  align-self: flex-end;
  padding: 8px 20px;
  border-radius: 6px;
}


.nav-side {
  --wechat-bg: #f8f8f8;
  /* 微信背景色 */
  --wechat-active-bg: #ededed;
  /* 激活状态背景 */
  --wechat-icon-color: #7f7f7f;
  /* 默认图标色 */
  --wechat-active-color: #000000;
  /* 激活状态颜色 */
  --wechat-border-color: #e6e6e6;
  /* 边框色 */

  background: var(--wechat-bg);
  width: 72px;
  height: 100vh;
  border-right: 1px solid var(--wechat-border-color);
  flex-direction: column;
  box-shadow: 1px 0 6px rgba(0, 0, 0, 0.1);
  padding: 16px 0;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.nav-items {
  flex: 1;
  width: 100%;
}

.nav-item {
  padding: 12px;
  margin: 8px 0;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  position: relative;
  display: flex;
  justify-content: center;

  &:hover {
    background: #f3f3f3;

    .nav-icon {
      color: #333;
      /* 悬停时图标颜色加深 */
    }
  }

  &.active {
    background: transparent !important;
    /* 移除背景色 */

    .nav-icon {
      color: #07c160 !important;
      /* 激活状态图标变绿 */
      filter: brightness(0.9);
      /* 颜色加深效果 */
    }
  }
}

/* 图标基础样式 */
.nav-icon {
  color: #666;
  transition: color 0.3s;
}

/* 徽章定位调整 */
.nav-badge,
.message-badge {
  position: absolute;
  top: 6px;
  right: 6px;

  .el-badge__content {
    background: #07c160;
    height: 18px;
    line-height: 18px;
    padding: 0 4px;
    font-size: 12px;
  }
}

/* 用户头像样式 */
.user-avatar {
  border: 2px solid #eaeaea;
  transition: border-color 0.3s;

  &:hover {
    border-color: #07c160;
  }
}


.el-avatar {
  border: 2px solid #eaeaea;
  transition: border-color 0.3s;

  &:hover {
    border-color: #07c160;
  }
}

.nav-icon {
  color: #666;
  transition: color 0.3s;
}

.nav-badge {
  position: absolute;
  top: 4px;
  right: 4px;
}

/*!* 调整左侧内容区 *!
.left-side {
  width: 280px;
  border-right: 1px solid #e5e5e5;
  display: flex;
  flex-direction: column;
}*/

.sub-header {
  padding: 16px;
  font-weight: 500;
  border-bottom: 1px solid #e5e5e5;
}

/*.user-list-scroll {
  height: 600px;
  --badge-size: 20px;
}*/

.user-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  cursor: pointer;
  transition: background-color 0.3s;
  border-bottom: 1px solid #ebeef5;
}

.user-item:hover {
  background: #f5f7fa;
}

/* 选中态样式覆盖 */
.active-item {
  background: #f5f7fa !important;
  transition: background 0.3s ease-in-out;
}

/* 适配暗黑模式 */
@media (prefers-color-scheme: dark) {
  .user-item.active-item .content-wrapper {
    background: #2d2d2d !important;
  }
}

.user-avatar-wrapper {
  position: relative;
  margin-right: 16px;
  flex-shrink: 0;
}

.user-avatar {
  width: 48px;
  height: 48px;
  border-radius: 6px;
  object-fit: cover;
}

.message-badge {
  position: absolute;
  top: 4px;
  right: 4px;
}

.online-dot {
  position: absolute;
  bottom: -3px;
  right: -3px;
  width: 14px;
  height: 14px;
  border: 2px solid #fff;
  border-radius: 50%;
  background: #67C23A;
}

.user-details {
  flex: 1;
  min-width: 0;
}

/*.header-line {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}*/

/*.user-name {
  font-weight: 600;
  color: #303133;
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}*/

/*.message-time {
  color: #909399;
  font-size: 12px;
  flex-shrink: 0;
  margin-left: 8px;
}*/

/*.last-message {
  color: #606266;
  font-size: 13px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  !* 确保容器不会无限扩展 *!
  max-width: 300px;
  display: flex;
  align-items: center;
  gap: 4px;
}*/
.user-list-scroll-search {
  position: absolute;
  top: 3px;
  /* 根据搜索框高度调整 */
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.96);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  z-index: 2;
  transition: all 0.3s ease;
}

.contact-header {
  padding: 12px;
  border-bottom: 1px solid #f0f0f0;
}

.contact-search-input {
  width: 100%;
}

.contact-search-input .el-icon {
  cursor: pointer;
  color: #409eff;
}

.contact-list-scroll {
  height: calc(100% - 60px);
}

.new-friend-item {
  display: flex;
  align-items: center;
  padding: 12px;
  cursor: pointer;
  border-bottom: 1px solid #f0f0f0;
}

.new-friend-item:hover {
  background-color: #f5f5f5;
}

.new-friend-icon {
  margin-right: 10px;
  font-size: 20px;
  color: #409eff;
}

.contact-item {
  display: flex;
  align-items: center;
  padding: 12px;
  cursor: pointer;
}

.contact-item:hover {
  background-color: #f5f5f5;
}

.contact-avatar {
  width: 40px;
  height: 40px;
  border-radius: 4px;
  margin-right: 12px;
}

.contact-name {
  font-size: 14px;
}

/* 弹窗蒙层 - 确保位于最顶层 */
.dialog-mask {
  position: fixed;
  top: 0;
  left: 0;
  z-index: 9999;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  backdrop-filter: blur(3px);
  /* 添加毛玻璃效果 */

  right: 0;
  bottom: 0;
}

/* 卡片容器 */
.user-card {
  overflow: hidden;
  position: relative;
  width: 480px;
  background: #fff;
  border-radius: 12px;
  padding: 28px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  animation: cardEnter 0.3s cubic-bezier(0.18, 0.89, 0.32, 1.28);
}

/* 入场动画 */
@keyframes cardEnter {
  from {
    transform: translateY(20px);
    opacity: 0;
  }

  to {
    transform: translateY(0);
    opacity: 1;
  }
}

/* 关闭按钮增强 */
.close-btn {
  z-index: 1;
  position: absolute;
  top: 16px;
  right: 16px;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s;
}

.close-btn:hover {
  background: #f5f5f5;
  transform: rotate(90deg);
}

.close-btn::before {
  //content: "×";
  font-size: 24px;
  color: #999;
  transition: color 0.3s;
}

.close-btn:hover::before {
  color: #666;
}

/* 用户信息区域 */
.card-header {
  display: flex;
  align-items: center;
  margin-bottom: 24px;
}

.avatar {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  background: #f0f2f5;
  margin-right: 16px;
  overflow: hidden;
}

.user-info h3 {
  margin: 0 0 8px 0;
  font-size: 20px;
  color: #1a1a1a;
  font-weight: 600;
}

.signature {
  color: #909399;
  font-size: 14px;
  line-height: 1.4;
  max-width: 240px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 详细信息区域 */
.detail-section {
  margin: 24px 0;
  padding: 16px 0;
  border-top: 1px solid #ebedf0;
  border-bottom: 1px solid #ebedf0;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  font-size: 14px;
}

.detail-item span:first-child {
  color: #909399;
  min-width: 72px;
}

.detail-item span:last-child {
  color: #303133;
  font-weight: 500;
}

/* 操作按钮优化 */
.action-buttons {
  display: flex;
  gap: 12px;
  margin-top: 20px;
}

.btn {
  flex: 1;
  padding: 10px 16px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn.primary {
  background: #409eff;
  color: white;
}

.btn.primary:hover {
  background: #66b1ff;
}

.btn:not(.primary) {
  background: #f5f5f5;
  color: #606266;
}

.btn:not(.primary):hover {
  background: #e5e5e5;
}

.btn.add-friend {
  background: #67c23a;
  color: white;
  width: 100%;
}

.btn.add-friend:hover {
  background: #85ce61;
}

/* 响应式处理 */
@media (max-width: 480px) {
  .user-card {
    width: 90%;
    min-width: 300px;
    padding: 20px;
  }

  .detail-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 4px;
  }
}

/* 新增样式 */
.friend-form {
  padding: 20px;
}

.form-header {
  position: relative;
  margin-bottom: 25px;
  display: flex;
  align-items: center;
}

.back-icon {
  font-size: 20px;
  color: #606266;
  cursor: pointer;
  transition: all 0.3s;
  margin-right: 15px;
}

.back-icon:hover {
  color: #409EFF;
  transform: translateX(-3px);
}

.form-header h3 {
  margin: 0;
  font-size: 18px;
  color: #303133;
}

.reason-input textarea {
  resize: none;
  padding: 12px;
  font-size: 14px;
  border-radius: 6px;
  transition: border-color 0.3s;
}

.reason-input textarea:focus {
  border-color: #409EFF;
}

.form-actions {
  margin-top: 25px;
  text-align: right;
}

.submit-btn {
  background: #67C23A;
  color: white;
  padding: 10px 30px;
  border-radius: 20px;
  transition: all 0.3s;
}

.submit-btn:hover:not(:disabled) {
  opacity: 0.9;
  transform: translateY(-1px);
}

.submit-btn:disabled {
  background: #EBEEF5;
  color: #C0C4CC;
  cursor: not-allowed;
}

/* 保持原有样式的基础上增加过渡效果 */
.main-content,
.friend-form {
  transition: all 0.3s ease;
}

/* 新好友列表弹窗弹窗内容 */
.dialog-content {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 400px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.15);
  z-index: 333;
}

.dialog-title {
  padding: 16px;
  margin: 0;
  border-bottom: 1px solid #ebeef5;
  font-size: 16px;
  color: #303133;
}

/* 申请列表 */
.application-list {
  max-height: 60vh;
  overflow-y: auto;
  padding: 8px 0;
}

/* 单个申请项 */
.application-item {
  display: flex;
  padding: 12px 16px;
  transition: background 0.3s;
}

.application-item:hover {
  background: #fafafa;
}

.user-avatar {
  flex-shrink: 0;
  margin-right: 12px;
}

.user-avatar img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.user-info {
  flex-grow: 1;
  min-width: 0;
}

.name-row {
  display: flex;
  align-items: baseline;
  margin-bottom: 4px;
}

.username {
  font-size: 14px;
  color: #303133;
  margin-right: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  flex-shrink: 0;
  max-width: 100%;
}

.username.truncate {
  display: inline-block;
  max-width: 6em;
  /* 根据字体大小调整，6个中文字约为6em */
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  vertical-align: bottom;
}

.wechat-id {
  font-size: 12px;
  color: #909399;
}

.apply-reason {
  margin: 0;
  font-size: 12px;
  color: #606266;
  line-height: 1.5;
}

/* 操作按钮 */
.action-buttons {
  flex-shrink: 0;
  margin-left: 12px;
  display: flex;
  align-items: center;
}

.btn {
  padding: 6px 12px;
  margin-left: 8px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  transition: all 0.3s;
}

.btn.accept {
  background: #67c23a;
  color: white;
}

.btn.accept:hover {
  background: #5daf34;
}

.btn.reject {
  background: #f56c6c;
  color: white;
}

.btn.reject:hover {
  background: #e65050;
}

.status-tag {
  font-size: 12px;
  padding: 4px 8px;
  border-radius: 4px;
}

.status-tag.accepted {
  color: #67c23a;
  background: #f0f9eb;
}

.status-tag.rejected {
  color: #f56c6c;
  background: #fef0f0;
}

/*统一搜索框和消息长度*/
/* 添加以下样式 */
.left-chat-container {
  width: 100%;
  /* 统一容器宽度 */
  height: 100vh;
  box-sizing: border-box;
}

/*.search-wrapper {
  padding: 12px;
  background: #fff;
}*/


/* 统一输入框样式 */
.search-wrapper .el-input {
  width: 100%;

  .el-input__inner {
    border-radius: 4px;
    padding-right: 40px;
    /* 给清除按钮留空间 */
  }
}

/* 统一用户项间距 */
.user-item {
  padding: 12px;
  margin: 4px 0;
  border-radius: 4px;
  transition: all 0.3s;

  &:hover {
    background: #f5f7fa;
  }
}

/*发起群聊按钮图标*/
.search-icon {
  cursor: pointer;
  padding: 8px;
  color: #606266;
  transition: color 0.3s;
  left: 70px;
}

.search-icon:hover {
  color: #409EFF;
}


/* 微信风格弹窗样式 */
.dialog-mask {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

/*.dialog-wrapper {
  width: 440px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 12px 24px rgba(0,0,0,0.1);
}*/

/*.dialog-header {
  padding: 16px;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}*/

.close-btn {
  cursor: pointer;
  font-size: 24px;
  color: #999;
}

/*.dialog-body {
  padding: 20px;
}*/

.input-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 8px;
  color: #666;
}

.wechat-input {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  transition: border-color 0.3s;
}

.wechat-input:focus {
  border-color: #07C160;
  outline: none;
}

.search-wrapper {
  position: relative;
}



/* 输入框聚焦时显示下拉 */
.wechat-input:focus+.friend-list {
  display: block;
  /* 纯CSS触发显示 ‌:ml-citation{ref="7" data="citationList"} */
}

/*.friend-item {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  position: relative; !* 建立定位上下文 *!
}*/

.friend-item:hover {
  background: #f5f5f5;
  /* 悬停反馈 ‌:ml-citation{ref="3,8" data="citationList"} */
}

.wechat-checkbox {
  margin-right: 12px;
  accent-color: #07C160;
  /* 复选框品牌色 ‌:ml-citation{ref="1" data="citationList"} */
  opacity: 0;
  /* 隐藏原生控件 */
  position: absolute;
  width: 18px;
  height: 18px;
}

.checkbox-wrapper {
  position: relative;
  margin-right: 12px;
  z-index: 1;
  /* 确保层级高于头像 */
}

.checkmark {
  display: inline-block;
  width: 18px;
  height: 18px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background: white;
}

/* 选中态样式 */
.wechat-checkbox:checked+.checkmark {
  background: #07C160 url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTYiIGhlaWdodD0iMTIiIHZpZXdCb3g9IjAgMCAxNiAxMiIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cGF0aCBkPSJNNS42IDExLjhMMC40IDYuNiAxLjggNS4yIDUuNiA5IDE0LjIgMC40IDE1LjYgMS44IDUuNiAxMS44eiIgZmlsbD0iI2ZmZiIvPjwvc3ZnPg==') no-repeat center;
  background-size: 12px;
}

.content-wrapper {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 8px;
}

.dialog-footer {
  padding: 16px;
  text-align: right;
  border-top: 1px solid #eee;
}

.confirm-btn,
.cancel-btn {
  padding: 8px 24px;
  border-radius: 4px;
  margin-left: 12px;
  cursor: pointer;
  transition: opacity 0.3s;
}

.confirm-btn {
  background: #07C160;
  color: white;
  border: none;
}

.cancel-btn {
  background: transparent;
  color: #666;
  border: 1px solid #ddd;
}

.confirm-btn:hover {
  opacity: 0.9;
}







.chat--group-container {
  display: flex;
  width: 800px;
  height: 600px;
  border: 1px solid #e5e5e5;
  font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
}

/* 左侧面板样式 */
.left-panel {
  width: 280px;
  border-right: 1px solid #e5e5e5;
}

/*.search-box {
  padding: 12px;
  border-bottom: 1px solid #e5e5e5;
}*/

.search-group-input {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #e5e5e5;
  border-radius: 4px;
  font-size: 14px;
}

.friend-list {
  height: calc(100% - 57px);
  overflow-y: auto;
}

/*.friend-item {
  display: flex;
  align-items: center;
  padding: 12px;
  cursor: pointer;
  position: relative;
}

.friend-item:hover {
  background-color: #f5f5f5;
}*/

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 12px;
}

.name {
  font-size: 14px;
  color: #333;
}

.check-mark {
  position: absolute;
  right: 15px;
  color: #09bb07;
  font-size: 18px;
}

/* 右侧面板样式 */
.right-panel {
  flex: 1;
  padding: 16px;
}

.selected-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.title {
  font-size: 14px;
  color: #999;
}

.clear-btn {
  color: #576b95;
  font-size: 14px;
  background: none;
  border: none;
  cursor: pointer;
}

.selected-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 16px;
  max-height: 450px;
  overflow-y: auto;
}

.selected-item {
  width: 80px;
  text-align: center;
}

.avatar-wrapper {
  position: relative;
  margin-bottom: 4px;
}

.remove-icon {
  position: absolute;
  top: -6px;
  right: -6px;
  width: 18px;
  height: 18px;
  background: #ff4d4f;
  color: white;
  border-radius: 50%;
  font-size: 14px;
  line-height: 18px;
  cursor: pointer;
}

.action-area {
  border-top: 1px solid #e5e5e5;
  padding-top: 16px;
}

.group-input {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #e5e5e5;
  border-radius: 4px;
  margin-bottom: 12px;
}

.submit-btn {
  width: 100%;
  padding: 10px;
  background: #07c160;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
}

.submit-btn:hover {
  background: #06ad56;
}


/* 微信风格基础样式 */
.wechat-style {
  font-family: -apple-system, BlinkMacSystemFont, 'Helvetica Neue', sans-serif;
  color: #333;
}

/* 遮罩层 */
.dialog-mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
}

.dialog-wrapper {
  width: 680px;
  background: white;
  border-radius: 8px;
  overflow: hidden;
}

/* 标题栏 */
.dialog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  border-bottom: 1px solid #e5e5e5;

  .title {
    font-size: 17px;
    font-weight: 500;
  }

  .close-btn {
    font-size: 24px;
    color: #999;
    cursor: pointer;
    padding: 0 8px;

    &:hover {
      color: #666;
    }
  }
}

/* 内容区布局 */
.dialog-body {
  display: flex;
  height: 420px;
}

/* 左侧面板 */
.left-panel {
  width: 280px;
  border-right: 1px solid #e5e5e5;

  .search-box {
    padding: 16px;
  }

  .wechat-search {
    width: 100%;
    height: 32px;
    padding: 0 12px;
    border: 1px solid #e5e5e5;
    border-radius: 4px;
    font-size: 14px;

    &:focus {
      border-color: #07c160;
    }
  }
}

.friend-list {
  height: calc(100% - 64px);
  overflow-y: auto;
}

.friend-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  cursor: pointer;
  position: relative;

  &:hover {
    background: #f5f5f5;
  }

  .user-avatar {
    width: 40px;
    height: 40px;
    border-radius: 4px;
    margin-right: 12px;
  }

  .name {
    font-size: 16px;
  }

  .selection-mark {
    position: absolute;
    right: 3px;
    width: 20px;
    height: 20px;
    border: 1px solid #ddd;
    border-radius: 50%;

    .check-icon {
      position: absolute;
      top: 3px;
      left: 3px;
      width: 12px;
      height: 6px;
      border: 2px solid #07c160;
      border-top: none;
      border-right: none;
      transform: rotate(-45deg);
    }
  }
}

/* 右侧面板 */
.right-panel {
  flex: 1;
  padding: 16px;
  display: flex;
  flex-direction: column;
}

.selected-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;

  .text {
    color: #999;
    font-size: 14px;
  }

  .clear-btn {
    color: #576b95;
    background: none;
    border: none;
    cursor: pointer;
  }
}

.selected-list {
  flex: 1;
  display: flex;
  flex-wrap: wrap;
  align-content: flex-start;
  gap: 12px;
  overflow-y: auto;
}

.selected-item {
  width: 72px;
  text-align: center;

  .avatar-wrapper {
    position: relative;
    margin-bottom: 4px;

    .user-avatar {
      width: 56px;
      height: 56px;
      border-radius: 4px;
    }

    .remove-btn {
      position: absolute;
      top: -6px;
      right: -6px;
      width: 20px;
      height: 20px;
      background: #ff4d4f;
      color: white;
      border-radius: 50%;
      font-size: 16px;
      line-height: 18px;
      cursor: pointer;
    }
  }

  .name {
    font-size: 12px;
    color: #666;
    display: block;
    text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap;
  }
}

.group-input {
  padding-top: 16px;
  border-top: 1px solid #e5e5e5;

  .wechat-input {
    width: 100%;
    height: 40px;
    padding: 0 12px;
    border: 1px solid #e5e5e5;
    border-radius: 4px;
    font-size: 14px;

    &:focus {
      border-color: #07c160;
    }
  }
}

/* 底部按钮 */
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 16px;
  padding: 16px;
  border-top: 1px solid #e5e5e5;

  .wechat-btn {
    height: 36px;
    padding: 0 24px;
    border: none;
    border-radius: 4px;
    font-size: 14px;
    cursor: pointer;

    &.cancel {
      background: #f5f5f5;
      color: #666;

      &:hover {
        background: #eee;
      }
    }

    &.primary {
      background: #07c160;
      color: white;

      &:hover {
        background: #06ad56;
      }
    }
  }
}

/* 抽屉样式 */
.drawer-mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 999;
  transition: opacity 0.3s;
}

.drawer-container {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  width: 280px;
  background: white;
  transform: translateX(100%);
  transition: transform 0.3s;
  z-index: 1000;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.15);
}

.drawer-container.show {
  transform: translateX(0);
}

.drawer-content {
  padding: 16px;
}

/* 群成员样式 */
.group-title {
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 12px;
}

.member-item {
  height: 70px;
  display: flex;
  align-items: center;
  padding: 8px;
  border-radius: 6px;
  transition: background 0.3s;
}

.member-item:hover {
  background: #f5f5f5;
}

.member-avatar {
  width: 40px;
  height: 40px;
  border-radius: 6px;
  margin-right: 12px;
}



/* 操作列表样式 */
.action-list {
  margin-top: 20px;
}

.action-item {
  padding: 12px;
  border-radius: 6px;
  transition: background 0.3s;
  cursor: pointer;
}

.action-item:hover {
  background: #f5f5f5;
}

.text-danger {
  color: #ff4d4f;
}

.action-item+.action-item {
  margin-top: 8px;
}

/*聊天框*/
/* 工具栏样式 */
.toolbar {
  display: flex;
  justify-content: space-between;
  padding: 8px 4px;
  opacity: 1 !important;
  visibility: visible !important;
}

/* 修复工具栏布局 */
.left-tools,
.right-tools {
  display: flex !important;
  /* 解除注释并增强权重 */
  gap: 8px;
}

/* 优化按钮容器 */
.tool-btn {
  position: relative;
  /* 为图标定位提供基准 */
  background: transparent !important;
  /* 清除可能存在的背景色 */
}

/* 重定义图标样式 */
.icon {
  display: block;
  width: 24px;
  height: 24px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-size: contain;
  background-repeat: no-repeat;
}

/* 添加颜色变量 */
:root {
  --icon-color: #000000;
  --icon-hover: #07C160;
}

/* 动态颜色控制 */
.tool-btn:hover .icon {
  filter: brightness(0.85);
}

/* 更新所有图标URL的fill值 */
.icon.voice {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cpath fill='%23000000' d='M12 14c1.66 0 3-1.34 3-3V5c0-1.66-1.34-3-3-3S9 3.34 9 5v6c0 1.66 1.34 3 3 3zm-1 1.93A7.33 7.33 0 0 1 5 11H3c0 3.07 2.24 5.62 5.13 6h.75c3.53 0 6.43-2.61 6.92-6h-2.02c-.48 2.28-2.4 4-4.78 4s-4.3-1.72-4.78-4H5c0 3.31 2.69 6 6 6v3h2v-3.07z'/%3E%3C/svg%3E");
  background-size: contain;
  background-repeat: no-repeat;
}

/* 其他图标同理更新fill值为var(--icon-color)的URL编码 */


/* 键盘图标 */
.icon.keyboard {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cpath fill='%23000000' d='M20 5H4c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V7c0-1.1-.9-2-2-2zm-8 3c1.66 0 3 1.34 3 3s-1.34 3-3 3-3-1.34-3-3 1.34-3 3-3zm4 10H8v-1c0-2 4-3.1 4-3.1s4 1.1 4 3.1v1z'/%3E%3C/svg%3E");
}

/* 表情图标 */
.icon.emoji {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cpath fill='%23000000' d='M12 22C6.486 22 2 17.514 2 12S6.486 2 12 2s10 4.486 10 10-4.486 10-10 10zm0-18c-4.411 0-8 3.589-8 8s3.589 8 8 8 8-3.589 8-8-3.589-8-8-8z'/%3E%3Ccircle cx='8.5' cy='10.5' r='1.5'/%3E%3Ccircle cx='15.5' cy='10.5' r='1.5'/%3E%3Cpath d='M12 17c-2.003 0-3.863-1.012-4.982-2.682l-1.743.97C6.314 17.325 8.974 19 12 19s5.686-1.675 6.725-4.712l-1.743-.97C15.863 15.988 14.003 17 12 17z'/%3E%3C/svg%3E");
}

/* 文件图标 */
.icon.file {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cpath fill='%23000000' d='M14 2H6c-1.1 0-2 .9-2 2v16c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V8l-6-6zM6 20V4h7v5h5v11H6z'/%3E%3C/svg%3E");
}

/* 视频图标 */
.icon.video {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cpath fill='%23000000' d='M17 10.5V7c0-.55-.45-1-1-1H4c-.55 0-1 .45-1 1v10c0 .55.45 1 1 1h12c.55 0 1-.45 1-1v-3.5l4 4v-11l-4 4z'/%3E%3C/svg%3E");
}

/* 电话图标 */
.icon.phone {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cpath fill='%23000000' d='M6.62 10.79c1.44 2.83 3.76 5.14 6.59 6.59l2.2-2.2c.27-.27.67-.36 1.02-.24 1.12.37 2.33.57 3.57.57.55 0 1 .45 1 1V20c0 .55-.45 1-1 1-9.39 0-17-7.61-17-17 0-.55.45-1 1-1h3.5c.55 0 1 .45 1 1 0 1.25.2 2.45.57 3.57.11.35.03.74-.25 1.02l-2.2 2.2z'/%3E%3C/svg%3E");
}



/* 输入区域 */
.input-area {
  margin-top: 8px;
}

/* 语音按钮 */
.voice-mode {
  display: flex;
  justify-content: center;
}

.voice-btn {
  width: 100%;
  height: 40px;
  border: 1px solid #e6e6e6;
  border-radius: 4px;
  background: #fff;
  color: #666;
  font-size: 14px;
  transition: all 0.2s;
}

.voice-btn:hover {
  background: #f0f0f0;
}

/* 文本输入框 */
.text-mode {
  display: flex;
  gap: 8px;
}

.message-input {
  flex: 1;
  min-height: 40px;
  max-height: 120px;
  padding: 8px 12px;
  border: 1px solid #e6e6e6;
  border-radius: 4px;
  font-size: 14px;
  line-height: 1.5;
  resize: none;
  transition: border-color 0.2s;
}

.message-input:focus {
  outline: none;
  border-color: #07c160;
  box-shadow: 0 0 0 2px rgba(7, 193, 96, 0.1);
}

.send-btn {
  padding: 6px 16px;
  background: #07c160;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.2s;
}

.send-btn:hover {
  background: #06ad56;
}

/* 表情面板 */
.emoji-panel {
  position: absolute;
  bottom: 60px;
  background: white;
  border: 1px solid #e6e6e6;
  border-radius: 8px;
  padding: 12px;
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.emoji-item {
  cursor: pointer;
  padding: 4px;
  font-size: 24px;
  text-align: center;
  border-radius: 4px;
  transition: background 0.2s;
}

.emoji-item:hover {
  background: #f5f5f5;
}

.hidden-file {
  display: none;
}

.bot-chat-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: #f5f5f5;
}

.bot-chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background: linear-gradient(180deg, #f0f2f5 0%, #ffffff 100%);
}

.message {
  display: flex;
  margin-bottom: 20px;
  gap: 12px;
}

.message.user {
  flex-direction: row-reverse;
}

.avatar img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.bubble {
  max-width: 70%;
  position: relative;
}

.bubble .content {
  padding: 12px 16px;
  border-radius: 12px;
  line-height: 1.5;
  font-size: 14px;
}

.message.bot .content {
  background: white;
  border: 1px solid #e5e7eb;
  border-radius: 12px 12px 12px 4px;
}

.message.user .content {
  background: #3875f6;
  color: white;
  border-radius: 12px 12px 4px 12px;
}

.status {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 4px;
  font-size: 12px;
  color: #666;
}

.message.user .status {
  justify-content: flex-end;
}

.typing-indicator {
  display: inline-flex;
  gap: 4px;
}

.dot {
  width: 6px;
  height: 6px;
  background: #999;
  border-radius: 50%;
  animation: bounce 1.4s infinite ease-in-out;
}

.dot:nth-child(2) {
  animation-delay: 0.2s;
}

.dot:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes bounce {

  0%,
  80%,
  100% {
    transform: translateY(0);
  }

  40% {
    transform: translateY(-4px);
  }
}

.bot-input-area {
  display: flex;
  gap: 12px;
  padding: 20px;
  border-top: 1px solid #e5e7eb;
  background: white;
}

textarea {
  flex: 1;
  padding: 12px;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  resize: none;
  min-height: 44px;
  max-height: 120px;
  font-family: inherit;
}

button {
  padding: 0 20px;
  background: #3875f6;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: opacity 0.2s;
}

button:disabled {
  background: #a0aec0;
  cursor: not-allowed;
}

.sending-indicator {
  display: inline-block;
  width: 20px;
  height: 20px;
  border: 2px solid #fff;
  border-top-color: transparent;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.bubble :deep() pre {
  background: #f8f8f8;
  padding: 12px;
  border-radius: 6px;
  overflow-x: auto;
}

.bubble :deep() code {
  font-family: 'JetBrains Mono', monospace;
  font-size: 14px;
}

.bubble :deep() ul,
.bubble :deep() ol {
  padding-left: 20px;
  margin: 8px 0;
}

.bubble :deep() blockquote {
  border-left: 4px solid #ddd;
  margin: 8px 0;
  padding-left: 12px;
  color: #666;
}

.example-showcase .el-dropdown-link {
  cursor: pointer;
  color: var(--el-color-primary);
  display: flex;
  align-items: center;
}

.message-username {
  font-size: 14px;
  color: #666;
  margin-bottom: 4px;
  /* 如果要做悬浮效果可以添加以下样式 */
  /* position: absolute;
  top: -18px;
  left: 0; */
}

.message-content-wrapper {
  flex: 1;
  position: relative;
}

.full-width-button {
  display: block;
  /* 或者 width: 100%; */
  width: 100%;
  /* 如果需要按钮宽度占满容器 */
  margin-bottom: 10px;
}
</style>
