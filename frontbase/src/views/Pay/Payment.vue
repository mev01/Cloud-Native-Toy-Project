<template>
  <div class="imp-container">
    <v-layout align-center data-aos="fade-right">
      <v-toolbar-title class="headline">
          <span><b>프리미엄</b></span>
          <span class="green--text"><b>&nbsp;결제</b></span>
      </v-toolbar-title>
      <br><br><br><br>
    </v-layout>
    <a-form
      :form="form"
      :label-col="{ span: 6 }"
      :wrapper-col="{ span: 18 }"
      :colon="false"
      labelAlign="left"
      @submit="handleSubmit"
    >
      <a-form-item label="PG사">
        <a-select
          v-decorator="['pg', { initialValue: 'html5_inicis' }]"
          size="large"
          @change="handlePg"
        >
          <a-select-option
            v-bind:key="pg.value"
            v-for="pg in pgs"
            :value="pg.value"
          >
            {{ pg.label }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="결제수단">
        <a-select
          v-decorator="['payMethod', { initialValue: 'card' }]"
          size="large"
          @change="handleMethod"
        >
          <a-select-option
            v-bind:key="method.value"
            v-for="method in methods"
            :value="method.value"
          >
            {{ method.label }}
          </a-select-option>
        </a-select>
      </a-form-item>
      
      <a-form-item label="입금기한" v-if="vbankDueVisible">
        <a-input
          v-decorator="[
            'vbankDue',
            {
              rules: [
                {
                  required: true,
                  message: '가상계좌 입금기한은 필수입력입니다',
                },
              ],
            },
          ]"
          placeholder="YYYYMMDDhhmm"
          size="large"
          type="number"
        />
      </a-form-item>
      <a-form-item label="사업자번호" v-if="bizNumVisible">
        <a-input
          v-decorator="[
            'bizNum',
            {
              rules: [
                { required: true, message: '사업자번호는 필수입력입니다' },
              ],
            },
          ]"
          size="large"
          type="number"
          readonly
        />
      </a-form-item>
      <a-form-item label="할부개월수" v-if="quotaVisible">
        <a-select v-decorator="['quota', { initialValue: '0' }]" size="large">
          <a-select-option
            v-bind:key="quota"
            v-for="quota in quotas"
            :value="quota"
          >
            {{
              quota === "0"
                ? "PG사 기본제공"
                : quota === "1"
                ? "일시불"
                : `${quota}개월`
            }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="주문번호">
        <a-input
          v-decorator="['merchantUid', { initialValue: initialMerchantUid }]"
          size="large"
          readonly
        />
      </a-form-item>
      <a-form-item label="주문명">
        <a-input
          v-decorator="['name', { initialValue: '혼자뭐하니 결제' }]"
          size="large"
          readonly
        />
      </a-form-item>
      <a-form-item label="결제금액">
        <a-input
          v-decorator="['amount', { initialValue: '100' }]"
          size="large"
          type="number"
          readonly
        />
      </a-form-item>
      <a-form-item label="이름">
        <a-input
          v-decorator="['buyerName', { initialValue: memberInfo.name }]"
          size="large"
        />
      </a-form-item>
      <a-form-item label="연락처">
        <a-input
          v-decorator="['buyerPhone', { initialValue: memberInfo.phone }]"
          size="large"
          type="number"
        />
      </a-form-item>
      <a-form-item label="이메일">
        <a-input
          v-decorator="['buyerEmail', { initialValue: memberInfo.email }]"
          size="large"
        />
      </a-form-item>
      <a-form-item label="주소">
        <a-input
          v-decorator="[
            'buyerAddr',
            { initialValue: memberInfo.address+' '+memberInfo.addressDetail },
          ]"
          size="large"
        />
      </a-form-item>
      <a-form-item label="우편번호">
        <a-input
          v-decorator="['buyerPostcode', { initialValue: memberInfo.zonecode }]"
          size="large"
          type="number"
        />
      </a-form-item>
      <a-button size="large" @click="handleGoBack"> 뒤로가기 </a-button>
      <a-button type="primary" html-type="submit" size="large">
        결제하기
      </a-button>
    </a-form>
  </div>
</template>

<script>
import { PGS } from '@/constants';
import Utils from '@/utils';
import { mapGetters } from "vuex";
export default {
  computed: {
    ...mapGetters(["memberInfo"])
  },
  data() {
    return {
      formLayout: 'horizontal',
      form: this.$form.createForm(this, { name: 'payment' }),
      initialMerchantUid: `mid_${new Date().getTime()}`,
      pgs: PGS,
      methods: Utils.getMethodsByPg(),
      quotas: Utils.getQuotaByPg(),
      initialMethod: 'card',
      vbankDueVisible: false,
      bizNumVisible: false,
      quotaVisible: true,
    };
  },
  methods: {
    handleSubmit(e) {
      e.preventDefault();
      this.form.validateFields((err, values) => {
        if (!err) {
          const {
            pg,
            payMethod,
            escrow,
            vbankDue,
            bizNum,
            quota,
            merchantUid,
            name,
            amount,
            buyerName,
            buyerTel,
            buyerEmail,
            buyerAddr,
            buyerPostcode,
          } = values;
          const { IMP } = window;
          IMP.init('imp23127199');
          const data = {
            pg,
            pay_method: payMethod,
            escrow,
            merchant_uid: merchantUid,
            name,
            amount,
            buyer_name: buyerName,
            buyer_tel: buyerTel,
            buyer_email: buyerEmail,
            buyer_addr: buyerAddr,
            buyer_postcode: buyerPostcode,
            niceMobileV2: true,
          };

          if (payMethod === 'vbank') {
            data.vbank_due = vbankDue;
            if (pg === 'danal_tpay') {
              data.biz_num = bizNum;
            }
          }
          if (payMethod === 'card') {
            data.display = {
              card_quota: quota,
            };
          }
          IMP.request_pay(data, this.callback);
        }
      });
    },
    handlePg(pg) {
      const newMethods = Utils.getMethodsByPg(pg);
      const [{ value }] = newMethods;

      this.methods = Utils.getMethodsByPg(pg);
      this.form.setFieldsValue({
        payMethod: value,
      });

      this.setVisible(pg, value);
    },
    handleMethod(method) {
      const pg = this.form.getFieldValue('pg');
      this.setVisible(pg, method);
    },
    setVisible(pg, method) {
      const vbankDueVisible = method === 'vbank';
      this.vbankDueVisible = vbankDueVisible;

      this.bizNumVisible = pg === 'danal_tpay' && vbankDueVisible;

      this.quotaVisible = method === 'card';
    },
    handleGoBack() {
      this.$router.push('/');
    },
    callback(response) {
      const query = {
        ...response,
        type: 'payment',
      };
      this.$router.push({ path: '/result', query });
    },
  },
};
</script>
