<template>
  <div>
    <el-upload
        class="upload-demo"
        action="javascript:void(0);"
        :before-upload="beforeUpload"
    >
      <el-button size="small" type="primary">点击上传</el-button>
      <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
    </el-upload>
    <img v-if="imageUrl" :src="imageUrl" alt="Preview" style="max-width: 300px;">
  </div>
</template>

<script>
import request from "@/utils/request";
import {ElMessage} from "element-plus";

export default {
  data() {
    return {
      imageUrl: '', // 用于预览的图片base64编码
    };
  },
  methods: {
    beforeUpload(file) {
      const isJPGorPNG = file.type === 'image/jpeg' || file.type === 'image/png';
      const isLt500K = file.size / 1024 < 5000;

      if (!isJPGorPNG) {
        console.error('上传图片只能是 JPG/PNG 格式!');
      }
      if (!isLt500K) {
        console.error('上传图片大小不能超过 500KB!');
      }

      if (isJPGorPNG && isLt500K) {
        // 将图片转换为base64
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => {
          this.imageUrl = reader.result; // 用于预览
          // 发送请求
          this.sendToBackend(reader.result);
        };
        return false; // 阻止默认上传行为
      }
    },
    async sendToBackend(imageBase64) {
      try {
        // 注意：实际开发中应使用POST请求，并且可能需要调整后端接口以接收base64字符串
        request.post("barCode/getId",imageBase64).then(res=>{
          if(res.code == 0)
          {
            let value = res.data
            console.log(res)
            ElMessage.success(value)
          }
          else {ElMessage.error("错误")}
        })
        // 这里可以处理后端返回的数据
      } catch (error) {
        console.error('请求错误:', error);
      }
    },
  },
};
</script>
