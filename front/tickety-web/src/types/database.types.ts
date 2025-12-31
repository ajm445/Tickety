export type Json =
  | string
  | number
  | boolean
  | null
  | { [key: string]: Json | undefined }
  | Json[]

export type Database = {
  // Allows to automatically instantiate createClient with right options
  // instead of createClient<Database, { PostgrestVersion: 'XX' }>(URL, KEY)
  __InternalSupabase: {
    PostgrestVersion: "14.1"
  }
  public: {
    Tables: {
      concerts: {
        Row: {
          artist: string
          booking_end_at: string
          booking_start_at: string
          concert_date: string
          created_at: string
          description: string | null
          id: string
          poster_url: string | null
          price_max: number
          price_min: number
          status: Database["public"]["Enums"]["concert_status"]
          title: string
          updated_at: string
          venue_id: string
        }
        Insert: {
          artist: string
          booking_end_at: string
          booking_start_at: string
          concert_date: string
          created_at?: string
          description?: string | null
          id?: string
          poster_url?: string | null
          price_max: number
          price_min: number
          status?: Database["public"]["Enums"]["concert_status"]
          title: string
          updated_at?: string
          venue_id: string
        }
        Update: {
          artist?: string
          booking_end_at?: string
          booking_start_at?: string
          concert_date?: string
          created_at?: string
          description?: string | null
          id?: string
          poster_url?: string | null
          price_max?: number
          price_min?: number
          status?: Database["public"]["Enums"]["concert_status"]
          title?: string
          updated_at?: string
          venue_id?: string
        }
        Relationships: [
          {
            foreignKeyName: "concerts_venue_id_fkey"
            columns: ["venue_id"]
            isOneToOne: false
            referencedRelation: "venues"
            referencedColumns: ["id"]
          },
        ]
      }
      payments: {
        Row: {
          amount: number
          created_at: string
          failed_at: string | null
          failure_reason: string | null
          id: string
          metadata: Json | null
          method: Database["public"]["Enums"]["payment_method"]
          paid_at: string | null
          payment_number: string
          pg_provider: string | null
          pg_transaction_id: string | null
          refund_reason: string | null
          refunded_amount: number | null
          refunded_at: string | null
          reservation_id: string
          status: Database["public"]["Enums"]["payment_status"]
          updated_at: string
          user_id: string
        }
        Insert: {
          amount: number
          created_at?: string
          failed_at?: string | null
          failure_reason?: string | null
          id?: string
          metadata?: Json | null
          method: Database["public"]["Enums"]["payment_method"]
          paid_at?: string | null
          payment_number: string
          pg_provider?: string | null
          pg_transaction_id?: string | null
          refund_reason?: string | null
          refunded_amount?: number | null
          refunded_at?: string | null
          reservation_id: string
          status?: Database["public"]["Enums"]["payment_status"]
          updated_at?: string
          user_id: string
        }
        Update: {
          amount?: number
          created_at?: string
          failed_at?: string | null
          failure_reason?: string | null
          id?: string
          metadata?: Json | null
          method?: Database["public"]["Enums"]["payment_method"]
          paid_at?: string | null
          payment_number?: string
          pg_provider?: string | null
          pg_transaction_id?: string | null
          refund_reason?: string | null
          refunded_amount?: number | null
          refunded_at?: string | null
          reservation_id?: string
          status?: Database["public"]["Enums"]["payment_status"]
          updated_at?: string
          user_id?: string
        }
        Relationships: [
          {
            foreignKeyName: "payments_reservation_id_fkey"
            columns: ["reservation_id"]
            isOneToOne: false
            referencedRelation: "reservations"
            referencedColumns: ["id"]
          },
        ]
      }
      reservation_seats: {
        Row: {
          created_at: string
          id: string
          price: number
          reservation_id: string
          seat_id: string
        }
        Insert: {
          created_at?: string
          id?: string
          price: number
          reservation_id: string
          seat_id: string
        }
        Update: {
          created_at?: string
          id?: string
          price?: number
          reservation_id?: string
          seat_id?: string
        }
        Relationships: [
          {
            foreignKeyName: "reservation_seats_reservation_id_fkey"
            columns: ["reservation_id"]
            isOneToOne: false
            referencedRelation: "reservations"
            referencedColumns: ["id"]
          },
          {
            foreignKeyName: "reservation_seats_seat_id_fkey"
            columns: ["seat_id"]
            isOneToOne: false
            referencedRelation: "seats"
            referencedColumns: ["id"]
          },
        ]
      }
      reservations: {
        Row: {
          cancellation_reason: string | null
          cancelled_at: string | null
          concert_id: string
          confirmed_at: string | null
          created_at: string
          expires_at: string | null
          id: string
          reservation_number: string
          status: Database["public"]["Enums"]["reservation_status"]
          total_amount: number
          updated_at: string
          user_id: string
        }
        Insert: {
          cancellation_reason?: string | null
          cancelled_at?: string | null
          concert_id: string
          confirmed_at?: string | null
          created_at?: string
          expires_at?: string | null
          id?: string
          reservation_number: string
          status?: Database["public"]["Enums"]["reservation_status"]
          total_amount: number
          updated_at?: string
          user_id: string
        }
        Update: {
          cancellation_reason?: string | null
          cancelled_at?: string | null
          concert_id?: string
          confirmed_at?: string | null
          created_at?: string
          expires_at?: string | null
          id?: string
          reservation_number?: string
          status?: Database["public"]["Enums"]["reservation_status"]
          total_amount?: number
          updated_at?: string
          user_id?: string
        }
        Relationships: [
          {
            foreignKeyName: "reservations_concert_id_fkey"
            columns: ["concert_id"]
            isOneToOne: false
            referencedRelation: "concerts"
            referencedColumns: ["id"]
          },
        ]
      }
      seats: {
        Row: {
          concert_id: string
          created_at: string
          grade: Database["public"]["Enums"]["seat_grade"]
          held_by: string | null
          held_until: string | null
          id: string
          price: number
          row_number: string
          seat_number: number
          section: string
          status: Database["public"]["Enums"]["seat_status"]
          updated_at: string
          version: number
        }
        Insert: {
          concert_id: string
          created_at?: string
          grade: Database["public"]["Enums"]["seat_grade"]
          held_by?: string | null
          held_until?: string | null
          id?: string
          price: number
          row_number: string
          seat_number: number
          section: string
          status?: Database["public"]["Enums"]["seat_status"]
          updated_at?: string
          version?: number
        }
        Update: {
          concert_id?: string
          created_at?: string
          grade?: Database["public"]["Enums"]["seat_grade"]
          held_by?: string | null
          held_until?: string | null
          id?: string
          price?: number
          row_number?: string
          seat_number?: number
          section?: string
          status?: Database["public"]["Enums"]["seat_status"]
          updated_at?: string
          version?: number
        }
        Relationships: [
          {
            foreignKeyName: "seats_concert_id_fkey"
            columns: ["concert_id"]
            isOneToOne: false
            referencedRelation: "concerts"
            referencedColumns: ["id"]
          },
        ]
      }
      venues: {
        Row: {
          address: string
          city: string
          created_at: string
          description: string | null
          id: string
          image_url: string | null
          name: string
          total_seats: number
          updated_at: string
        }
        Insert: {
          address: string
          city: string
          created_at?: string
          description?: string | null
          id?: string
          image_url?: string | null
          name: string
          total_seats: number
          updated_at?: string
        }
        Update: {
          address?: string
          city?: string
          created_at?: string
          description?: string | null
          id?: string
          image_url?: string | null
          name?: string
          total_seats?: number
          updated_at?: string
        }
        Relationships: []
      }
    }
    Views: {
      [_ in never]: never
    }
    Functions: {
      [_ in never]: never
    }
    Enums: {
      concert_status:
        | "SCHEDULED"
        | "OPEN"
        | "SOLD_OUT"
        | "CANCELLED"
        | "COMPLETED"
      payment_method:
        | "CREDIT_CARD"
        | "DEBIT_CARD"
        | "BANK_TRANSFER"
        | "KAKAO_PAY"
        | "NAVER_PAY"
        | "TOSS_PAY"
      payment_status:
        | "PENDING"
        | "PROCESSING"
        | "COMPLETED"
        | "FAILED"
        | "REFUNDED"
        | "PARTIAL_REFUNDED"
      reservation_status: "PENDING" | "CONFIRMED" | "CANCELLED" | "EXPIRED"
      seat_grade: "VIP" | "R" | "S" | "A" | "B"
      seat_status: "AVAILABLE" | "HELD" | "RESERVED" | "SOLD"
    }
    CompositeTypes: {
      [_ in never]: never
    }
  }
}

type DatabaseWithoutInternals = Omit<Database, "__InternalSupabase">

type DefaultSchema = DatabaseWithoutInternals[Extract<keyof Database, "public">]

export type Tables<
  DefaultSchemaTableNameOrOptions extends
    | keyof (DefaultSchema["Tables"] & DefaultSchema["Views"])
    | { schema: keyof DatabaseWithoutInternals },
  TableName extends DefaultSchemaTableNameOrOptions extends {
    schema: keyof DatabaseWithoutInternals
  }
    ? keyof (DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Tables"] &
        DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Views"])
    : never = never,
> = DefaultSchemaTableNameOrOptions extends {
  schema: keyof DatabaseWithoutInternals
}
  ? (DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Tables"] &
      DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Views"])[TableName] extends {
      Row: infer R
    }
    ? R
    : never
  : DefaultSchemaTableNameOrOptions extends keyof (DefaultSchema["Tables"] &
        DefaultSchema["Views"])
    ? (DefaultSchema["Tables"] &
        DefaultSchema["Views"])[DefaultSchemaTableNameOrOptions] extends {
        Row: infer R
      }
      ? R
      : never
    : never

export type TablesInsert<
  DefaultSchemaTableNameOrOptions extends
    | keyof DefaultSchema["Tables"]
    | { schema: keyof DatabaseWithoutInternals },
  TableName extends DefaultSchemaTableNameOrOptions extends {
    schema: keyof DatabaseWithoutInternals
  }
    ? keyof DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Tables"]
    : never = never,
> = DefaultSchemaTableNameOrOptions extends {
  schema: keyof DatabaseWithoutInternals
}
  ? DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Tables"][TableName] extends {
      Insert: infer I
    }
    ? I
    : never
  : DefaultSchemaTableNameOrOptions extends keyof DefaultSchema["Tables"]
    ? DefaultSchema["Tables"][DefaultSchemaTableNameOrOptions] extends {
        Insert: infer I
      }
      ? I
      : never
    : never

export type TablesUpdate<
  DefaultSchemaTableNameOrOptions extends
    | keyof DefaultSchema["Tables"]
    | { schema: keyof DatabaseWithoutInternals },
  TableName extends DefaultSchemaTableNameOrOptions extends {
    schema: keyof DatabaseWithoutInternals
  }
    ? keyof DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Tables"]
    : never = never,
> = DefaultSchemaTableNameOrOptions extends {
  schema: keyof DatabaseWithoutInternals
}
  ? DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Tables"][TableName] extends {
      Update: infer U
    }
    ? U
    : never
  : DefaultSchemaTableNameOrOptions extends keyof DefaultSchema["Tables"]
    ? DefaultSchema["Tables"][DefaultSchemaTableNameOrOptions] extends {
        Update: infer U
      }
      ? U
      : never
    : never

export type Enums<
  DefaultSchemaEnumNameOrOptions extends
    | keyof DefaultSchema["Enums"]
    | { schema: keyof DatabaseWithoutInternals },
  EnumName extends DefaultSchemaEnumNameOrOptions extends {
    schema: keyof DatabaseWithoutInternals
  }
    ? keyof DatabaseWithoutInternals[DefaultSchemaEnumNameOrOptions["schema"]]["Enums"]
    : never = never,
> = DefaultSchemaEnumNameOrOptions extends {
  schema: keyof DatabaseWithoutInternals
}
  ? DatabaseWithoutInternals[DefaultSchemaEnumNameOrOptions["schema"]]["Enums"][EnumName]
  : DefaultSchemaEnumNameOrOptions extends keyof DefaultSchema["Enums"]
    ? DefaultSchema["Enums"][DefaultSchemaEnumNameOrOptions]
    : never

export type CompositeTypes<
  PublicCompositeTypeNameOrOptions extends
    | keyof DefaultSchema["CompositeTypes"]
    | { schema: keyof DatabaseWithoutInternals },
  CompositeTypeName extends PublicCompositeTypeNameOrOptions extends {
    schema: keyof DatabaseWithoutInternals
  }
    ? keyof DatabaseWithoutInternals[PublicCompositeTypeNameOrOptions["schema"]]["CompositeTypes"]
    : never = never,
> = PublicCompositeTypeNameOrOptions extends {
  schema: keyof DatabaseWithoutInternals
}
  ? DatabaseWithoutInternals[PublicCompositeTypeNameOrOptions["schema"]]["CompositeTypes"][CompositeTypeName]
  : PublicCompositeTypeNameOrOptions extends keyof DefaultSchema["CompositeTypes"]
    ? DefaultSchema["CompositeTypes"][PublicCompositeTypeNameOrOptions]
    : never

export const Constants = {
  public: {
    Enums: {
      concert_status: [
        "SCHEDULED",
        "OPEN",
        "SOLD_OUT",
        "CANCELLED",
        "COMPLETED",
      ],
      payment_method: [
        "CREDIT_CARD",
        "DEBIT_CARD",
        "BANK_TRANSFER",
        "KAKAO_PAY",
        "NAVER_PAY",
        "TOSS_PAY",
      ],
      payment_status: [
        "PENDING",
        "PROCESSING",
        "COMPLETED",
        "FAILED",
        "REFUNDED",
        "PARTIAL_REFUNDED",
      ],
      reservation_status: ["PENDING", "CONFIRMED", "CANCELLED", "EXPIRED"],
      seat_grade: ["VIP", "R", "S", "A", "B"],
      seat_status: ["AVAILABLE", "HELD", "RESERVED", "SOLD"],
    },
  },
} as const
