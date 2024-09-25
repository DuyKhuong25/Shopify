package com.ecom.common.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "order_track")
public class OrderTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "note", length = 200)
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    private OrderStatus status;

    @Column(name = "tine_updated", nullable = false)
    private Date timeUpdated;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderTrack() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Date getTimeUpdated() {
        return timeUpdated;
    }

    public void setTimeUpdated(Date timeUpdated) {
        this.timeUpdated = timeUpdated;
    }

    @Override
    public String toString() {
        return "OrderTrack{" +
                "id=" + id +
                ", note='" + note + '\'' +
                ", status=" + status +
                ", timeUpdated=" + timeUpdated +
                ", order=" + order.getId() +
                '}';
    }
}
