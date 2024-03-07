<script setup lang="ts">
import {onMounted, ref} from "vue";
  import axios from "axios";
import {useRouter} from "vue-router";

  const props = defineProps({
    postId: {
      type: [Number, String],
      require: true,
    },
  });

  const post = ref({
    id: 0,
    title: "",
    content: ""
  });

  const router = useRouter();
  const moveToEdit = () => {
    router.push({name: "edit", params: {postId: props.postId}})
  };

  const moveToDelete = () => {
    axios.delete(`/api/posts/${props.postId}`)
    router.go(-1)
  }

  onMounted(() => {
    axios.get(`/api/posts/${props.postId}`)
        .then(response => {
          post.value = response.data;
        })
  });
</script>

<template>
  <el-row class="mt-3">
    <el-col>
      <div class="title>">
        {{ post.title }}
      </div>
    </el-col>
  </el-row>
  <el-row class ="mt-3">
    <div class="content">
      {{ post.content }}
    </div>
  </el-row>

  <el-button type="warning" @click="moveToEdit()">수정</el-button>
  <el-button type="warning" @click="moveToDelete()">삭제</el-button>
</template>

<style scoped lang="scss">
.title {
  font-size: 1.6rem;
  font-weight: 600;
  color: #383838;
  margin: 0;
}

.content {
  font-size: 0.95rem;
  margin-top: 12px;
  color: #616161;
  white-space: break-spaces;
  line-height: 1.5;
}
</style>