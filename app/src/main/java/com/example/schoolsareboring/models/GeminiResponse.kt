package com.example.schoolsareboring.models

data class GeminiResponse(
    val candidates: List<Candidate>,
    val usageMetadata: UsageMetadata,
    val modelVersion: String,
)

data class Candidate(
    val content: ResContent,
    val finishReason: String,
    val citationMetadata: CitationMetadata,
    val avgLogprobs: Double,
)

data class ResContent(
    val parts: List<ResPart>,
    val role: String,
)

data class ResPart(
    val text: String,
)

data class CitationMetadata(
    val citationSources: List<CitationSource>,
)

data class CitationSource(
    val startIndex: Long,
    val endIndex: Long,
    val uri: String,
)

data class UsageMetadata(
    val promptTokenCount: Long,
    val candidatesTokenCount: Long,
    val totalTokenCount: Long,
    val promptTokensDetails: List<PromptTokensDetail>,
    val candidatesTokensDetails: List<CandidatesTokensDetail>,
)

data class PromptTokensDetail(
    val modality: String,
    val tokenCount: Long,
)

data class CandidatesTokensDetail(
    val modality: String,
    val tokenCount: Long,
)
