<template>
  <el-button type="primary" size="small" @click="showNoOrder()">支付</el-button>
  <!-- 支付界面 -->
  <el-dialog v-model="orderDialogVisible" width="60%" center modal-append-to-body>
    <span>
      <el-descriptions width="80%" class="margin-top" title="支付订单" :column="1" border>
         <el-descriptions-item>
         <template slot="label">
         <i class="el-icon-user"></i>
        酒店名称
         </template>
           {{ curOrder.hotel }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
         <i class="el-icon-house"></i>
            房间类型
         </template>
             {{ curOrder.room }}
       </el-descriptions-item>
                  <el-descriptions-item>
          <template slot="label">
         <i class="el-icon-food"></i>
            有无早餐
         </template>
             {{ curOrder.breakfast }}
       </el-descriptions-item>
                       <el-descriptions-item>
          <template slot="label">
         <i class="el-icon-coin"></i>
        需要支付金额
         </template>
            {{ curOrder.price }}
       </el-descriptions-item>
                   <el-descriptions-item>
          <template slot="label">
         <i class="el-icon-time"></i>
        预计入住时间
         </template>
            {{ curOrder.checkIn }}
       </el-descriptions-item>
                    <el-descriptions-item>
          <template slot="label">
         <i class="el-icon-time"></i>
        预计退房时间
         </template>
            {{ curOrder.checkOut }}
       </el-descriptions-item>
                   <el-descriptions-item>
          <template slot="label">
         <i class="el-icon-time"></i>
        订单提交时间
         </template>
            {{ curOrder.time }}
       </el-descriptions-item>
      </el-descriptions>
    </span>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="pay">支付</el-button>
        <el-button @click="orderDialogVisible = false">取消</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref } from 'vue';
import { ElMessage } from "element-plus";

// 使用Composition API
const curOrder = ref({
  hotel: "东篱客栈",
  room: "豪华双人房",
  breakfast: "含早",
  price: "588元",
  checkIn: "2023-04-01 14:00",
  checkOut: "2023-04-02 12:00",
  time: "2023-03-25 10:00:00",
});

const orderDialogVisible = ref(false);

function showNoOrder(row = curOrder.value) {
  // 如果需要处理外部传入的实际row数据，此处逻辑可以调整
  curOrder.value = row;
  orderDialogVisible.value = true;
}

function pay() {
  window.open('http://localhost:8090/alipay/pay?subject=%E4%B8%9C%E5%8C%97%E8%8F%9C&traceNo=100014&totalAmount=100');
}
</script>

<style scoped>
/* 样式代码保持不变 */
</style>