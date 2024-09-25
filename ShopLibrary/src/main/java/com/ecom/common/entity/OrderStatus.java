package com.ecom.common.entity;

public enum OrderStatus {
    PENDING {
        @Override
        public String defaultDescription() {
            return "Đơn hàng đã đặt và chờ xác nhận";
        }
    },

    PROCESSING{
        @Override
        public String defaultDescription() {
            return "Đơn hàng đã xác nhận và đang xử lý";
        }
    },

    SHIPPING{
        @Override
        public String defaultDescription() {
            return "Shipper đã lấy hàng và đang giao hàng";
        }
    },

    SUCCESS{
        @Override
        public String defaultDescription() {
            return "Đơn hàng đã được giao thành công";
        }
    },

    CANCEL{
        @Override
        public String defaultDescription() {
            return "Đơn hàng đã bị hủy";
        }
    },

    RETURNED{
        @Override
        public String defaultDescription() {
            return "Khách hàng đã yêu cầu hoàn trả";
        }
    };

    public abstract String defaultDescription();
}
