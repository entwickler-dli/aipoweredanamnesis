package com.anamnesis.demo.dao;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "medical_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer historyId;

    @OneToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "id", nullable = false, unique = true)
    private Patient patient;

    @ElementCollection
    @CollectionTable(name = "chronic_conditions", joinColumns = @JoinColumn(name = "history_id"))
    @Column(name = "condition")
    private List<String> chronicConditions;

    @ElementCollection
    @CollectionTable(name = "diagnosis_history", joinColumns = @JoinColumn(name = "history_id"))
    private List<DiagnosisRecord> diagnosisHistory;

    @ElementCollection
    @CollectionTable(name = "surgical_history", joinColumns = @JoinColumn(name = "history_id"))
    private List<SurgicalRecord> surgicalHistory;

    @ElementCollection
    @CollectionTable(name = "allergies", joinColumns = @JoinColumn(name = "history_id"))
    private List<AllergyRecord> allergies;

    @ElementCollection
    @CollectionTable(name = "immunizations", joinColumns = @JoinColumn(name = "history_id"))
    private List<ImmunizationRecord> immunizationStatus;

    @ElementCollection
    @CollectionTable(name = "visit_summaries", joinColumns = @JoinColumn(name = "history_id"))
    private List<VisitSummary> visitSummaries;

    @ElementCollection
    @CollectionTable(name = "active_medications", joinColumns = @JoinColumn(name = "history_id"))
    private List<ActiveMedication> activeMedications;

    @ElementCollection
    @CollectionTable(name = "lab_results", joinColumns = @JoinColumn(name = "history_id"))
    private List<LabResult> recentLabResults;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    public Integer getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Integer historyId) {
        this.historyId = historyId;
    }

    public List<String> getChronicConditions() {
        return chronicConditions;
    }

    public void setChronicConditions(List<String> chronicConditions) {
        this.chronicConditions = chronicConditions;
    }

    public List<DiagnosisRecord> getDiagnosisHistory() {
        return diagnosisHistory;
    }

    public void setDiagnosisHistory(List<DiagnosisRecord> diagnosisHistory) {
        this.diagnosisHistory = diagnosisHistory;
    }

    public List<SurgicalRecord> getSurgicalHistory() {
        return surgicalHistory;
    }

    public void setSurgicalHistory(List<SurgicalRecord> surgicalHistory) {
        this.surgicalHistory = surgicalHistory;
    }

    public List<AllergyRecord> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<AllergyRecord> allergies) {
        this.allergies = allergies;
    }

    public List<ImmunizationRecord> getImmunizationStatus() {
        return immunizationStatus;
    }

    public void setImmunizationStatus(List<ImmunizationRecord> immunizationStatus) {
        this.immunizationStatus = immunizationStatus;
    }

    public List<VisitSummary> getVisitSummaries() {
        return visitSummaries;
    }

    public void setVisitSummaries(List<VisitSummary> visitSummaries) {
        this.visitSummaries = visitSummaries;
    }

    public List<ActiveMedication> getActiveMedications() {
        return activeMedications;
    }

    public void setActiveMedications(List<ActiveMedication> activeMedications) {
        this.activeMedications = activeMedications;
    }

    public List<LabResult> getRecentLabResults() {
        return recentLabResults;
    }

    public void setRecentLabResults(List<LabResult> recentLabResults) {
        this.recentLabResults = recentLabResults;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}

